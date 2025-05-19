package br.jus.tjro.gabinete.scheduled;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.service.local.PessoaDocumentoService;
import br.jus.tjro.gabinete.service.local.ProcessoParteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
public class BuscaDocumentosDasPessoas {

    @Autowired
    private PessoaDocumentoService pessoaDocumentoService;

    @Autowired
    private ProcessoParteService processoParteService;

    private final Logger looger = LoggerFactory.getLogger(BuscaDocumentosDasPessoas.class);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean executaPorProcesso(Processo processo) {
        try {
            List<ProcessoParte> partesDoProcesso = processoParteService.retornaTodasAsPartesPorProcesso(processo);
            for (ProcessoParte processoParte : partesDoProcesso){
                pessoaDocumentoService.importaOuAtualizaDocumentosDaPessoa(processoParte,processo.getSistema());
            }
            return true;
        } catch (Exception e) {
            looger.error("Problema na importacao do processo id: "+processo.getId()+" "+processo.getNumeroProcesso(),e);
            return false;
        }
    }
}
