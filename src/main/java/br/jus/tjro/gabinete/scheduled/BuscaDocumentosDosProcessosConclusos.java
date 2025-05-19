package br.jus.tjro.gabinete.scheduled;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.service.local.processo.documento.ProcessoDocumentoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BuscaDocumentosDosProcessosConclusos {

    @Autowired
    private ProcessoDocumentoService service;
    
    private final Logger looger = LoggerFactory.getLogger(BuscaDocumentosDosProcessosConclusos.class);

    public Boolean executaPorProcesso(Processo processo, int pagina) {
        try {
            service.importaOuAtualizaDocumentosProcesso(processo, pagina);
            return true;
        } catch (Throwable e) {
            looger.error("Problema na importacao do processo id: "+processo.getId()+" "+processo.getNumeroProcesso(),e);
            return false;
        }
    }
}
