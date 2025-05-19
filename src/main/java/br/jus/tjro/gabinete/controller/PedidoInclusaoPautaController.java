package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.PedidoInclusaoPauta;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.NotFoundException;


@Controller
@RequestMapping("pedido-inclusao-pauta")
public class PedidoInclusaoPautaController {
    private final ProcessosRepository processosRepository;

    @Autowired
    public PedidoInclusaoPautaController(
        ProcessosRepository processosRepository) {
        this.processosRepository = processosRepository;
    }

    @GetMapping("/{idProcesso}")
    public ResponseEntity<PedidoInclusaoPauta> getPauta(@PathVariable("idProcesso") Long idProcesso) {
        var pauta = processosRepository.findById(idProcesso).map(it -> {
                try {
                    return it.minutaEmElaboracao();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).map(it -> it.flatMap(Minuta::getPedidoInclusaoPauta).orElseThrow(() -> new NotFoundException("Não foi possível encontrar a pauta do processo")))
            .orElseThrow(() -> new NotFoundException("Não foi possível encontrar a pauta do processo"));
        return ResponseEntity.ok().body(pauta);
    }
}
