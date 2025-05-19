package br.jus.tjro.gabinete.scheduled;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.service.local.ProcessoMovimentoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BuscaMovimentosDosProcessos {

    @Autowired
    private ProcessoMovimentoService service;

    private final Logger looger = LoggerFactory.getLogger(BuscaMovimentosDosProcessos.class);

    public Boolean executaPorProcesso(Processo processo) {
        try {
            looger.info("Busca movimentos do processo "+processo.getNumeroProcesso());
            service.importaOuAtualizaMovimentosDoProcesso(processo);
            return true;
        } catch (Exception e) {
            looger.error("Problema na importacao do processo id: "+processo.getId()+" "+e.getMessage(),e);
            return false;
        }
    }
}
