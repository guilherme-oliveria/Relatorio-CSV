package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.transiente.Pessoa;
import br.jus.tjro.gabinete.service.remoto.PessoaRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("pessoas")
public class PessoaController {
    @Autowired
    PessoaRemotoService pessoaRemotoService;

    @GetMapping("/{sistema}")
    public ResponseEntity<PageImpl<Pessoa>> pesquisar(@PathVariable("sistema") FonteDadosEnum fonte,
                                                      @RequestParam("query") String query) throws Exception {
        return ok(pessoaRemotoService.pesquisarPessoas(query, fonte));
    }
}
