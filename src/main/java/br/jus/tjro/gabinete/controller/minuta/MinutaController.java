package br.jus.tjro.gabinete.controller.minuta;

import br.jus.tjro.gabinete.interfaces.ModeloDocumentoService;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.ContainerMinutaSemelhancaIA;
import br.jus.tjro.gabinete.model.gab.transiente.MinutaEmLote;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.MinutaAnexoService;
import br.jus.tjro.gabinete.service.local.MinutaService;
import br.jus.tjro.gabinete.service.local.PedidoInclusaoPautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("minutas")
public class MinutaController {

    private final MinutaService minutaService;
    private final MinutaAnexoService minutaAnexoService;
    private final ProcessosRepository processosRepository;
    private final ModeloDocumentoService modeloDocumento;

    private final AuthenticationUsuarioService auth;
    private final PedidoInclusaoPautaService pautaService;

    @Autowired
    public MinutaController(MinutaService minutaService,
                            MinutaAnexoService minutaAnexoService,
                            ProcessosRepository processosRepository,
                            PedidoInclusaoPautaService pautaService,
                            ModeloDocumentoService modeloDocumento, AuthenticationUsuarioService auth) {
        this.minutaService = minutaService;
        this.minutaAnexoService = minutaAnexoService;
        this.processosRepository = processosRepository;
        this.modeloDocumento = modeloDocumento;
        this.auth = auth;
        this.pautaService = pautaService;
    }

    @GetMapping("/minutas-semelhantes/{idOrgaoJulgador}/{idProcessoReferencia}")
    @PostAuthorize("valida")
    public ResponseEntity<ContainerMinutaSemelhancaIA> minutasSemelhantes(@PathVariable() String idOrgaoJulgador, @PathVariable() Long idProcessoReferencia) throws Exception {
        return ResponseEntity.ok(minutaService.pesquisarMinutasParaAssinarSimilares(idOrgaoJulgador, idProcessoReferencia));
    }


    @GetMapping("/{idProcesso}")
    @Transactional
    public ResponseEntity<Minuta> recuperarMinuta(@PathVariable("idProcesso") Long idProcesso, Authentication usuario) throws Exception {
        Minuta minuta = minutaService.recuperaMinutaNaoAssinadoDoProcesso(auth.getUsuario(usuario), idProcesso);
        return ResponseEntity.ok(minuta);
    }

    @PostMapping("/{idProcesso}")
    @Transactional
    public ResponseEntity<Minuta> salvarMinuta(@RequestBody Minuta minuta, @PathVariable("idProcesso") long idProcesso,
                                               Authentication usuario)
        throws Exception {
        var user = auth.getUsuario(usuario);
        minuta = minutaService.salvarComVersao(minuta,user);
        minuta = pautaService.talvezGeraPauta(minuta,user);
        minutaService.setMovimentosEhComplementosFromTpu(minuta);
        return ResponseEntity.ok().body(minuta);
    }

    @GetMapping("/lista-anexo/{idMinuta}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<MinutaAnexo>> listaAnexo(@PathVariable("idMinuta") Long idMinuta, Authentication usuario) throws Exception {
        List<MinutaAnexo> anexos = this.minutaAnexoService.anexosFromMinuta(idMinuta);
        if(anexos.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(anexos);
    }

    // TODO melhorar o codigo levar metodo para o controller da assinatura e
    // encapsular a logica de renderizar e salvar a minuta
    @GetMapping("/stream/{idMinuta}")
    @Transactional
    public ResponseEntity<Resource> recuperarBinarioParaAssinaturaDocumentoHtml(@PathVariable("idMinuta") Long id, Authentication auth)
        throws Exception {
        Minuta minuta = minutaService.findOne(id);
        Usuario usuario = this.auth.getUsuario(auth);
        Optional<Processo> processoOpt = this.processosRepository.findById(minuta.getIdProcesso());
        if(processoOpt.isEmpty())
            throw new Exception("Nao foi possivel recuperar o processo de id "+id);
        Processo processo = processoOpt.get();
        minuta.renderizarHtml(modeloDocumento, usuario, processo, true);
        minuta = minutaService.save(minuta);
        InputStreamResource resource = new InputStreamResource(minuta.getMinutaHtmlRenderizadoInputStream());
        return ResponseEntity.ok().body(resource);
    }

    @GetMapping("/movimento/remover/{idMinuta}/{idMovimento}")
    public ResponseEntity removerMovimento(@PathVariable("idMinuta") Long idMinuta,
                                           @PathVariable("idMovimento") Long idMovimento) throws Exception {
        minutaService.removeMovimento(idMinuta, idMovimento);
        return ResponseEntity.ok().body("");
    }

    @GetMapping("/movimento/adicionar/{idMinuta}/{idMovimento}")
    public ResponseEntity<MinutaMovimento> adicionarMovimento(@PathVariable("idMinuta") Long idMinuta,
                                                           @PathVariable("idMovimento") Long idMovimento) throws Exception {
        return ResponseEntity.ok().body(minutaService.adicionaMovimento(idMinuta, idMovimento));
    }

    @PostMapping("/minutar-em-lote")
    public ResponseEntity<List<Long>> minutarEmLote(@RequestBody MinutaEmLote minutaEmLote, Authentication usuario)
        throws Exception {
        return ResponseEntity.ok().body(this.minutaService.minutarEmLote(auth.getUsuario(usuario), minutaEmLote));
    }
}
