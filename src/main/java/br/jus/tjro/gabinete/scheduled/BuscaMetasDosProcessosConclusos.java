package br.jus.tjro.gabinete.scheduled;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import br.jus.tjro.gabinete.service.local.ProcessoTagService;
import br.jus.tjro.gabinete.service.local.TagService;
import br.jus.tjro.gabinete.service.remoto.meta.MetasProcessoRemotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BuscaMetasDosProcessosConclusos {

    private final MetasProcessoRemotoService metasProcessoRemotoService;

    private final ProcessoTagService processoTagService;

    private final TagService tagService;

    @Autowired
    public BuscaMetasDosProcessosConclusos(MetasProcessoRemotoService metasProcessoRemotoService, ProcessoTagService processoTagService, TagService tagService) {
        this.metasProcessoRemotoService = metasProcessoRemotoService;
        this.processoTagService = processoTagService;
        this.tagService = tagService;
    }


    private final Logger logger = LoggerFactory.getLogger(BuscaMetasDosProcessosConclusos.class);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean executaPorProcesso(Processo processo) {
        try
        {
            var metasProcesso = metasProcessoRemotoService.obterMetasParaNumeroProcesso(processo.getNumeroProcesso());

            var tagsOrgaoJulgador = tagService.obterPorOrgaoJulgador(processo.getOrgaoJulgadorObj().getId());
            for (var meta : metasProcesso) {
                tagsOrgaoJulgador.stream().filter(m -> m.getTag().equals(meta)).findFirst().ifPresentOrElse(tag -> {
                    processoTagService.save(montarProcessoTag(processo, tag));
                }, () -> {
                    var novaTag = new Tag();
                    novaTag.setOrgaoJulgador(processo.getOrgaoJulgadorObj().getId());
                    novaTag.setTag(meta);
                    novaTag.setCorHexadecimal("#ff0000");
                    novaTag = tagService.save(novaTag);
                    processoTagService.save(montarProcessoTag(processo, novaTag));
                });
            }
            return true;
        } catch (Exception e) {
            logger.error("Problema na importação de metas para o processo id " + processo.getId() + " " +processo.getNumeroProcesso(), e);
            return false;
        }
    }

    private ProcessoTag montarProcessoTag(Processo processo, Tag tag) {
        var ptag = new ProcessoTag();
        ptag.setProcesso(processo);
        ptag.setTag(tag);
        ptag.setPersistente(true);
        return ptag;
    }

}
