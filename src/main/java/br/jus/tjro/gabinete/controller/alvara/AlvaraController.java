package br.jus.tjro.gabinete.controller.alvara;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.alvara.Alvara;
import br.jus.tjro.gabinete.model.gab.alvara.ContaJudicial;
import br.jus.tjro.gabinete.model.gab.alvara.InstituicaoFinanceira;
import br.jus.tjro.gabinete.model.gab.alvara.TipoConta;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.alvara.AlvaraService;
import br.jus.tjro.gabinete.service.local.MinutaService;
import br.jus.tjro.gabinete.service.remoto.alvara.ContasJudiciaisRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("alvara")
public class AlvaraController {

    @Autowired
    private MinutaService minutaService;

    @Autowired
    private ProcessosRepository processosRepository;

    @Autowired
    private AlvaraService alvaraService;

    @Autowired
    private ContasJudiciaisRemotoService contaService;

    @Autowired
    private AuthenticationUsuarioService auth;

    @PostMapping
    public ResponseEntity<Alvara> salvaInformacoesBancariasAlvara(@RequestBody Alvara alvara) throws Exception {
        return ResponseEntity.ok().body(alvaraService.salvarInformacoesBancariasAlvara(alvara));
    }

    @GetMapping("/{idProcesso}")
    public ResponseEntity<Alvara> buscaInformacoesBancariasAlvara(@PathVariable("idProcesso") Long idProcesso) throws Exception {
        Minuta minuta = minutaService.pegaUltimaMinutaByProcesso(idProcesso);
        return ResponseEntity.ok().body(minuta.getAlvara());
    }

    @GetMapping("/contas/{idProcesso}")
    public ResponseEntity<List<ContaJudicial>> buscaContasJudiciais(@PathVariable("idProcesso") Long idProcesso, Authentication authentication) throws ServicoRemotoException {
        Optional<Processo> optProcesso = processosRepository.findById(idProcesso);
        if (optProcesso.isPresent()) {
            List<ContaJudicial> byProcesso = contaService.findByProcesso(optProcesso.get().numeroProcessoSemFormatacao(), auth.getUsuario(authentication));
            return ResponseEntity.ok().body(byProcesso);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/bancos")
    public List<Map<String, String>> buscaInstituicoesFinanceiras() {
        return InstituicaoFinanceira.getList().stream().map(i -> Map.of(i.getCodigo(), i.getDescricao())).collect(Collectors.toList());
    }

    @GetMapping("/contas/numero-processo/{nrProcesso}")
    public ResponseEntity<List<ContaJudicial>> buscaContasJudiciaisPorNrProcesso(@PathVariable("nrProcesso") String nrProcesso, Authentication authentication) throws ServicoRemotoException {
        return ResponseEntity.ok().body(contaService.findByProcesso(nrProcesso, auth.getUsuario(authentication)));
    }

    @GetMapping("/partes/processo/{idProcesso}")
    public ResponseEntity<?> buscaParteProcessoPorIdProcessoETipoPolo(@PathVariable("idProcesso") long idProcesso) throws Exception {
        return alvaraService.buscaParteProcessoPorIdProcesso(idProcesso);
    }

    @GetMapping("/tipo-conta")
    public ResponseEntity<?> buscaTiposConta() {
        return ResponseEntity.ok().body(TipoConta.getListaComCodigo());
    }
}
