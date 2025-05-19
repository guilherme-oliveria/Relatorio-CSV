package br.jus.tjro.gabinete.controller.processo;

import br.jus.tjro.gabinete.listener.model.processo.listener.LocalizadorListener;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoTagRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import br.jus.tjro.gabinete.service.local.ProcessoTagService;
import br.jus.tjro.gabinete.service.local.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("processo-tag")
public class ProcessoTagController {

    private final ProcessoTagService processoTagService;
    private final ProcessoTagRepository processoTagRepository;
    private final ProcessoService processoService;
    private final AuthenticationUsuarioService auth;
    private final TagService tagService;
    private final LocalizadorListener localizadorListener;

    @Autowired
    public ProcessoTagController(AuthenticationUsuarioService auth,
                                 ProcessoTagService processoTagService,
                                 ProcessoService processoService,
                                 TagService tagService,
                                 ProcessoTagRepository processoTagRepository, LocalizadorListener localizadorListener){
        this.processoTagService = processoTagService;
        this.processoService = processoService;
        this.auth = auth;
        this.processoTagRepository = processoTagRepository;
        this.tagService = tagService;
        this.localizadorListener = localizadorListener;
    }

    //TODO refazer esse metodo mais eficiente sem precisar ir duas vezes no banco
    @GetMapping("processo/{id}")
    public List<ProcessoTag> ObterPorProcessoId(@PathVariable("id") long processoId) throws Exception {
        var processo = processoService.findOne(processoId);
        return processoTagService.obterPorProcesso(processo);
    }

    @PostMapping("/")
    public ResponseEntity<List<ProcessoTag>> salvar(@RequestBody ProcessoTag[] processoTags) throws Exception {
        List<ProcessoTag> lista = new ArrayList<>();

        for (int i=0; i < processoTags.length; i++) {
            var processo = processoService.findOne(processoTags[i].getProcesso().getId());
            processoTags[i].setProcesso(processo);
            lista.add(processoTags[i]);
        }
        List<ProcessoTag> updated = processoTagService.save(lista);
        localizadorListener.send(updated);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("deletar/{idProcesso}/{idTag}")
    @Transactional
    public void deletar(
        @PathVariable("idProcesso") Long idProcesso,
        @PathVariable("idTag") Long idTag,
        Authentication user) throws Exception {
        Optional<Processo> processo = processoService.findById(idProcesso);
        if(processo.isEmpty())
            throw new Exception("O processo não existe");
        List<ProcessoTag> processoTag = processoTagRepository.findAllByProcessoAndTag(processo.get(), new Tag(idTag));
        if(processoTag == null || processoTag.size() == 0)
            throw new Exception("A tag não existe nesse processo");
        Usuario usuario = auth.getUsuario(user);
        processoTag.stream()
            .filter(it -> !it.getTag().isDeSistema())
            .forEach(t -> t.deletar(usuario.getNome() + " CPF" + usuario.getCpf()));
        List<ProcessoTag> updated = processoTagRepository.saveAll(processoTag);
        localizadorListener.send(updated);
    }


}
