package br.jus.tjro.gabinete.scheduled;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.service.local.ProcessoParteEnderecoService;
import br.jus.tjro.gabinete.service.local.ProcessoParteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
public class BuscaEnderecosDasPartesDosProcessos {

    @Autowired
    private ProcessoParteEnderecoService processoParteEnderecoService;

    private final ProcessoParteService processoParteService;
    
    private final Logger looger = LoggerFactory.getLogger(BuscaEnderecosDasPartesDosProcessos.class);

    @Autowired
    public BuscaEnderecosDasPartesDosProcessos(ProcessoParteService processoParteService){
        this.processoParteService = processoParteService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean executaPorProcesso(Processo processo) {

        List<ProcessoParte> todasAsPartesPorProcesso = processoParteService.retornaTodasAsPartesPorProcesso(processo);

        for (ProcessoParte processoParte : todasAsPartesPorProcesso) {
            try {
                // importa/atualiza endereço das partes de um processo
                processoParteEnderecoService.importaOuAtualizaEnderecoDaParteDoProcesso(processoParte);
            } catch (Exception e) {
                looger.error("Problema na importacao do processo id: "+processo.getId()+" "+processo.getNumeroProcesso(),e);
                return false;
            }
        }
        return true;
    }
}
