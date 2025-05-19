package br.jus.tjro.gabinete.controller.minuta;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebida;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebidaStatus;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.filter.ProcessoFilter;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasRecebidasRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.MinutaRecebidaService;
import br.jus.tjro.gabinete.service.local.MinutaService;
import brave.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("minutas-recebidas")
public class MinutasRecebidaController {

    @Autowired
    public MinutasRecebidaController(MinutasRecebidasRepository minutasRecebidasRepository,
                                     ProcessosRepository processosRepository,
                                     AuthenticationUsuarioService auth,
                                     MinutaRecebidaService minutaRecebidaService) {
        this.minutasRecebidasRepository = minutasRecebidasRepository;
        this.processosRepository = processosRepository;
        this.auth = auth;
        this.minutaRecebidaService = minutaRecebidaService;
    }

    private final MinutasRecebidasRepository minutasRecebidasRepository;
    private final ProcessosRepository processosRepository;
    private final AuthenticationUsuarioService auth;
    private final MinutaRecebidaService minutaRecebidaService;

    private final Logger looger = LoggerFactory.getLogger(MinutasRecebidaController.class);

    @GetMapping
    public Page<Processo> processosMinutasRecebidas(ProcessoFilter filtro,
                                                    @RequestHeader("oj") String idOJ,
                                                    Authentication user,
                                                    Pageable page){
        List<String> idsOjs = idOJ.equals("-1") ? auth.getUsuario(user).getOrgaosJulgadores() : List.of(idOJ);
        return processosRepository.findComMinutasRecebidas(filtro,idsOjs,page);
    }

    @GetMapping("/processo/{idProcesso}")
    public ResponseEntity<List<MinutaRecebida>> getMinutaRecebida(@PathVariable("idProcesso") Long idProcesso){
        return ResponseEntity.ok(minutasRecebidasRepository.findByProcessoAndStatus(new Processo(idProcesso), MinutaRecebidaStatus.PENDENTE));
    }

    @PostMapping("/aprovar/{id}")
    public ResponseEntity<MinutaRecebida> aprovarMinutaRecebida(@PathVariable("id") Long idMinuta, Authentication auth) throws Exception {
        MinutaRecebida minutaRecebida = minutasRecebidasRepository.findById(idMinuta)
            .orElseThrow(()-> new IllegalArgumentException("Minuta recebida não encontrada."));
        return ResponseEntity.ok(this.minutaRecebidaService.aprovar(minutaRecebida, this.auth.getUsuario(auth)));
    }

}

