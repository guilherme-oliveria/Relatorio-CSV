package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import br.jus.tjro.gabinete.service.local.RevisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("revisor")
public class RevisorController {

    private final AuthenticationUsuarioService auth;
    private final RevisorService revisorService;

    @Autowired
    public RevisorController(AuthenticationUsuarioService auth, ProcessoService processoService, RevisorService revisorService) {
        this.auth = auth;
        this.revisorService = revisorService;
    }

    @GetMapping("lista-paginada")
    public ResponseEntity<Page<Processo>> getProcessosRevisor(Pageable pageable, Authentication user) {
        return ResponseEntity.ok(revisorService.getProcessosPorOJETarefaRevisarPaginada(pageable, auth.getUsuario(user).getOrgaosJulgadoresCompleto()));
    }

    @GetMapping("/tem-revisor")
    public ResponseEntity<Boolean> verificaSePossuoRevisor(@RequestParam(value = "idOrgaoJulgador", required = false) String idOrgaoJulgador) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(revisorService.verificaSePossuoRevisor(idOrgaoJulgador));
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/processo/{id}/anexo/{idRelatorio}")
    public ResponseEntity<MinutaAnexo> getAnexoRelatorio(@PathVariable("id") long id, @PathVariable("idRelatorio") String idRelatorio) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(revisorService.getAnexoRelatorio(id, idRelatorio));
    }

    @PostMapping("/processo/envia")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<Boolean> enviaProcessoParaRevisor(@RequestBody Long id, Authentication user) {
        try {
            revisorService.enviaProcessoParaRevisor(id, auth.getUsuario(user));
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
