package br.jus.tjro.gabinete.controller.minuta;

import br.jus.tjro.gabinete.model.gab.question.QuestionBase;
import br.jus.tjro.gabinete.repository.QuestionBaseRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("questions")
public class QuestionsController {


    private final QuestionBaseRepository repository;
    private final ProcessosRepository processosRepository;

    @Autowired
    public QuestionsController(QuestionBaseRepository repository, ProcessosRepository processosRepository) {
        this.repository = repository;
        this.processosRepository = processosRepository;
    }

    @GetMapping("/{$idProcesso}")
    public ResponseEntity<Set<QuestionBase>> getQuestions(Long idProcesso) throws Exception {
        return ResponseEntity.ok().body(repository.findAll());
    }
}

