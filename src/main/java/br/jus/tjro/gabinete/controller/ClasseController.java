package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.service.local.tpu.TpuClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("classe")
public class ClasseController {

    @Autowired
    private TpuClasseService tpuArvoreService;

    @GetMapping("/arvore")
    public List<TpuClasse> arvore(Long id) throws Exception {
        return this.tpuArvoreService.getArvoreClasses(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TpuClasse> classe(@PathVariable("id") Long id) throws Exception {
        TpuClasse classe = this.tpuArvoreService.getClasse(id);
        return ResponseEntity.ok().body(classe);
    }


}
