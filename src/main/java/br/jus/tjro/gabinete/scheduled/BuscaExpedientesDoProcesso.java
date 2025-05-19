package br.jus.tjro.gabinete.scheduled;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.service.local.ProcessoMovimentoService;
import br.jus.tjro.gabinete.service.local.ProcessoParteExpedienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BuscaExpedientesDoProcesso {

    @Autowired
    private ProcessoParteExpedienteService service;

    private final Logger looger = LoggerFactory.getLogger(BuscaExpedientesDoProcesso.class);

    public Boolean executaPorProcesso(Processo processo) {
        try {
            service.importaOuAtualizaExpedientesDoProcesso(processo);
            return true;
        } catch (Exception e) {
            looger.error("Problema na importacao do processo id: "+processo.getId()+" "+e.getMessage(),e);
            return false;
        }
    }
}
