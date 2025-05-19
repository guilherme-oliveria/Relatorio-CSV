package br.jus.tjro.gabinete.scheduled;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.service.local.ProcessoParteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Component
public class BuscaPartesDosProcessos {

    private final ProcessoParteService processoParteService;

    private final Logger looger = LoggerFactory.getLogger(BuscaPartesDosProcessos.class);

    @Autowired
    public BuscaPartesDosProcessos(ProcessoParteService processoParteService){
        this.processoParteService = processoParteService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean executaPorProcesso(Processo processo) {
        try {
            processoParteService.importaOuAtualizaPartesProcesso(processo);
            return true;
        } catch (Throwable e) {
            looger.error("Problema na importacao do processo id: "+processo.getId()+" "+processo.getNumeroProcesso(),e);
            return false;
        }
    }
}
