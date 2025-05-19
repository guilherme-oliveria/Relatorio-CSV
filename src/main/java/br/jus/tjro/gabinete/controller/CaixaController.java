package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.interfaces.Tarefa;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.localizador.CaixaWrapper;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.localizador.CaixasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("caixas")
public class CaixaController {

    @Autowired
    private CaixasRepository caixasRepository;

    @Autowired
    private List<Tarefa> tarefas;

    @GetMapping("/")
    public ResponseEntity<List<Caixa>> pegaCaixasDoOrgaoJulgador(@RequestHeader("oj") String idOJ) {
        List<TarefaEnum> tarefasVisiveis = new ArrayList<TarefaEnum>();

        for (Tarefa t : tarefas) {
            if (t.isTarefaVisivel())
                tarefasVisiveis.add(t.getTarefa());
        }

        List<Caixa> caixas = caixasRepository.obtemListaCaixas(tarefasVisiveis, new OrgaoJulgador(idOJ));
        return caixas != null ? ResponseEntity.ok(caixas) : ResponseEntity.noContent().build();
    }

    @GetMapping("/todas")
    public ResponseEntity<List<CaixaWrapper>> pegaCaixasDoSistema() {

        List<Caixa> caixas = caixasRepository.findByTodos();
        ArrayList<CaixaWrapper> caixaWrappers = new ArrayList<CaixaWrapper>();
        for (Caixa item : caixas) {
            caixaWrappers.add(new CaixaWrapper(item.getId(), item.getNome()));
        }
        return caixaWrappers != null ? ResponseEntity.ok(caixaWrappers) : ResponseEntity.noContent().build();
    }

    @GetMapping("/porid/{id}")
    public ResponseEntity<CaixaWrapper> pegaCaixaPorId(@PathVariable("id") Integer id) {
        Caixa caixa = caixasRepository.findById(id).get();
        CaixaWrapper caixaWapper = new CaixaWrapper(caixa.getId(), caixa.getNome());
        return caixaWapper != null ? ResponseEntity.ok(caixaWapper) : ResponseEntity.noContent().build();
    }


}
