package br.jus.tjro.gabinete.scheduled;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.tag.TagImport;
import br.jus.tjro.gabinete.service.local.ProcessoTagService;
import br.jus.tjro.gabinete.service.local.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class BuscaTagsDosProcessos {

    @Autowired
    private TagService service;

    @Autowired
    private ProcessoTagService processoTagService;
    private final Logger looger = LoggerFactory.getLogger(BuscaTagsDosProcessos.class);

    public Boolean executaPorProcesso(Processo processo) {
        try {
            List<String> listTag = new ArrayList<>();
            service.importaOuAtualizaTagsDoProcesso(processo,listTag);
            processoTagService.atualizaTagImportPorProcesso(processo, listTag);
            return true;
        } catch (Exception e) {
            looger.error("Problema na importacao do processo id: "+processo.getId()+" "+processo.getNumeroProcesso(),e);
            return false;
        }
    }

}
