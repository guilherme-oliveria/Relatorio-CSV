package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.transiente.status.servicos.StatusServicoResposta;
import br.jus.tjro.gabinete.service.remoto.status.servicos.StatusServicosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("status-servicos")
@PreAuthorize("hasAuthority('ADMIN')")
public class StatusServicosController {

    @Autowired
    private StatusServicosService statusServicosService;


    @GetMapping("pg")
    public ResponseEntity<StatusServicoResposta> statusPjePG() throws ServicoRemotoException {
        return ResponseEntity.ok().body(this.statusServicosService.statusPjePGWebService());
    }

    @GetMapping("sg")
    public ResponseEntity<StatusServicoResposta> statusPjeSG() throws ServicoRemotoException {
        return ResponseEntity.ok().body(this.statusServicosService.statusPjeSGWebService());
    }

}
