package br.jus.tjro.gabinete.controller.minuta;

import br.jus.tjro.gabinete.interfaces.ModeloDocumentoService;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloDocumentoContador;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinutaContador;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador.AgrupadorModeloEnum;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinuta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.ModeloMinutaCabecalhoRodape;
import br.jus.tjro.gabinete.model.gab.transiente.VariavelTemplate.VariavelTemplate;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.repository.remoto.ModeloDocumentoRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.ModeloMinutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@RestController
public class ModeloMinutaController {

    private final ProcessosRepository processosRepository;
    private final AuthenticationUsuarioService authService;
    private final ModeloDocumentoService modeloDocumento;
    private final ModeloMinutaService modeloMinutaService;
    private final ModeloDocumentoRepository repository;
    private final Function<Usuario, String> getOj = (u) -> u.getOrgaosJulgadoresCompleto().stream().findFirst().map(OrgaoJulgador::getId).orElse("");

    @Autowired
    public ModeloMinutaController(
        ProcessosRepository processosRepository,
        AuthenticationUsuarioService authService,
        ModeloDocumentoService modeloDocumento,
        ModeloDocumentoRepository repository,
        ModeloMinutaService modeloMinutaService) {
        this.processosRepository = processosRepository;
        this.authService = authService;
        this.modeloDocumento = modeloDocumento;
        this.repository = repository;
        this.modeloMinutaService = modeloMinutaService;
    }

    @GetMapping(value = {"/modelos-minutas/variaveis", "/modelo-documentos/variaveis"})
    public ResponseEntity<List<VariavelTemplate>> variaveisDeTemplate(Authentication usuario) throws Exception {
        Usuario usuarioObj = authService.getUsuario(usuario);
        return ResponseEntity.ok(modeloDocumento.getVariaveisTemplate(usuarioObj.getToken()));
    }

    @GetMapping(value = "/modelos-minutas/{tipo}")
    public Page<ModeloMinuta> index(@PathVariable("tipo") String tipoConsulta,
                                    @RequestParam(value = "query", required = false) String query,
                                    @RequestParam(value = "vara", required = false) String idVara,
                                    @RequestParam(value = "idTipoDocumento", required = false) String idTipoDocumento,
                                    @RequestParam(value= "tags", required = false) List<String> tags,
                                    Authentication usuario, Pageable pageable) throws Exception {
        AgrupadorModeloEnum tipo = AgrupadorModeloEnum.valueOf(tipoConsulta.toUpperCase());
        Usuario usuarioObj = authService.getUsuario(usuario);
        BuscaModelo buscaModelo = new BuscaModelo(usuarioObj.getCpf(), idVara, usuarioObj.getOrgaosJulgadores(), query, idTipoDocumento, tags);
        return modeloDocumento.filterPaginated(pageable, tipo, buscaModelo,usuarioObj);
    }

    @GetMapping(value = "/modelo-documentos")
    public Page<ModeloMinuta> modeloDocumentos(@RequestParam(value = "grupo", required = false) String grupo,
                                               @RequestParam(value = "query", required = false) String query,
                                               @RequestParam(value = "vara", required = false) String idVara,
                                               @RequestParam(value = "idTipoDocumento", required = false) String idTipoDocumento,
                                               @RequestParam(value= "tags", required = false) List<String> tags,
                                               Authentication usuario, Pageable pageable) throws Exception {
        Usuario usuarioObj = authService.getUsuario(usuario);
        String oj = ofNullable(idVara).filter(s -> !s.isBlank()).orElse(getOj.apply(usuarioObj));
        return index(grupo, query, oj, idTipoDocumento, tags, usuario, pageable);
    }

    @PutMapping(value = "/modelo-documentos")
    public ResponseEntity<ModeloMinuta> updateDocs(@RequestBody ModeloMinuta modelo, Authentication usuario) throws Exception {
        return update(modelo, usuario);
    }

    @DeleteMapping(value = "/modelo-documentos/{id}")
    public void deleteDocs(@PathVariable("id") String id, Authentication usuario) throws Exception {
        this.update(id, usuario);
    }

    @GetMapping(value = { "/modelos-minutas/show/{id}", "/modelo-documentos/{id}"})
    public ResponseEntity<ModeloMinuta> show(@PathVariable("id") String id, Authentication usuario) throws Exception {
        Optional<ModeloMinuta> modelo = repository.findById(id,authService.getUsuario(usuario));
        return modelo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/modelos-minutas/edit/{id}")
    //TODO autorizacao
    public ResponseEntity<ModeloMinuta> edit(@PathVariable("id") String id, Authentication usuario)
        throws Exception {
        Optional<ModeloMinuta> modelo = repository.findById(id,authService.getUsuario(usuario));
        return modelo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/modelos-minutas/update")
    public ResponseEntity<ModeloMinuta> update(@RequestBody ModeloMinuta modelo, Authentication usuario) throws Exception {
        return ResponseEntity.ok(repository.save(modelo, authService.getUsuario(usuario)));
    }

    @PostMapping(value = "/modelos-minutas/delete/{id}")
    public void update(@PathVariable("id") String id, Authentication usuario)
        throws Exception {
        repository.delete(id, authService.getUsuario(usuario));
    }

    @PostMapping(value = "/modelos-minutas/copiar-para-usuario/{id}/orgao-julgador/{idOrgaoJulgador}")
    public ResponseEntity<ModeloMinuta> copiarParaUsuario(@PathVariable String id, @PathVariable String idOrgaoJulgador, Authentication usuario) throws Exception {
        return ResponseEntity.ok(modeloDocumento.copiarParaUsuario(id, idOrgaoJulgador, authService.getUsuario(usuario)));
    }

    @GetMapping(value = "/modelos-minutas/totais/{vara}")
    public ModeloMinutaContador totais(@PathVariable("vara") String lotacaoAtual,
                                       @RequestParam("tipo_minuta") String idTipoDocumento, Authentication usuario) throws Exception {
        Usuario usuarioObj = authService.getUsuario(usuario);
        BuscaModelo buscaModelo = new BuscaModelo(usuarioObj.getCpf(), lotacaoAtual, usuarioObj.getOrgaosJulgadores(),"", idTipoDocumento);
        return modeloDocumento.totais(buscaModelo, usuarioObj);
    }

    @GetMapping(value = "/modelos-minutas/cabecalho-rodape/{id_orgao}")
    public ModeloMinutaCabecalhoRodape cabecalhoRodape(@PathVariable("id_orgao") String idOrgao) {
        return modeloMinutaService.getCabecalhoERodape();
    }

    @GetMapping(value = "/modelos-minutas/renderizar/{modelos}/{processo}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> cabecalhoRodape(@PathVariable("modelos") String idModeloMinuta,
                                                  @PathVariable("processo") Long idProcesso, Authentication usuario) throws Exception {
        Processo processo = this.processosRepository.findById(idProcesso).orElseThrow();
        Usuario usuarioobj = authService.getUsuario(usuario);
        ModeloMinuta modelo = this.repository.findById(idModeloMinuta,usuarioobj).get();
        return ResponseEntity.ok().body(modeloDocumento.renderizarVariavel(usuarioobj,processo,modelo.getTemplate()));
    }

    @PostMapping(value = "/modelos-minutas/renderizar-variavel/{processo}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> renderizarVariavel(@PathVariable("processo") Long idProcesso,
                                                     @RequestBody String template,
                                                     Authentication usuario) throws Exception {
        Processo processo = this.processosRepository.findById(idProcesso).orElseThrow();
        return ResponseEntity.ok().body(modeloDocumento.renderizarVariavel(authService.getUsuario(usuario), processo, template));
    }

    @GetMapping(value = {"/modelos-minutas/agrupadores", "/modelo-documentos/agrupadores"})
    public ResponseEntity<List<ModeloDocumentoContador>> agrupadores(@RequestHeader(value = "oj", required = false) String idOJ,
                                                                 @RequestParam(value = "query", required = false) String query,
                                                                 @RequestParam(value = "idTipoDocumento", required = false) String idTipoDocumento,
                                                                 Authentication usuario) throws Exception {
        Usuario usuarioObj = authService.getUsuario(usuario);
        BuscaModelo buscaModelo = new BuscaModelo(
            usuarioObj.getCpf(),
            ofNullable(idOJ).filter(s -> !s.isBlank()).orElse(getOj.apply(usuarioObj)),
            usuarioObj.getOrgaosJulgadores(),query,idTipoDocumento);
        return ResponseEntity.ok().body(modeloDocumento.totaisDocumento(buscaModelo, usuarioObj));
    }

}
