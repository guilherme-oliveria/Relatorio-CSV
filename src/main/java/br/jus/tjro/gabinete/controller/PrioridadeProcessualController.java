package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.processo.PrioridadeProcessual;
import br.jus.tjro.gabinete.service.local.PrioridadeProcessualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("prioridade-processual")
public class PrioridadeProcessualController {

    @Autowired
    private PrioridadeProcessualService prioridadeProcessualService;

    @GetMapping
    public ResponseEntity<List<PrioridadeProcessual>> buscaPrioridadesProcessuais() throws Exception {

        List<PrioridadeProcessual> listaPrioridades = prioridadeProcessualService.getPrioridadesProcessuais();

        return listaPrioridades.size() > 1 ? ResponseEntity.ok(listaPrioridades)
            : ResponseEntity.noContent().build();
    }
}
