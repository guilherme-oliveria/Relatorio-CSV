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
@RequestMapping("classes")
public class ClasseJudicialController {

    @Autowired
    private TpuClasseService tpuArvoreService;

    @GetMapping("/arvore")
    public ResponseEntity<List<TpuClasse>> classeEmArvore() throws Exception {
        return classeEmArvore(null);
    }

    @GetMapping("/arvore/{id}")
    public ResponseEntity<List<TpuClasse>> classeEmArvore(@PathVariable Long id) throws Exception {
        List<TpuClasse> classeArvore = tpuArvoreService.getArvoreClasses(id);
        return classeArvore != null ? ResponseEntity.ok(classeArvore) : ResponseEntity.noContent().build();
    }
}
