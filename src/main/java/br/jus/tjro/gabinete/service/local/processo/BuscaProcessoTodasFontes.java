package br.jus.tjro.gabinete.service.local.processo;

import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.WrapperBuscaProcesso;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoRemotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscaProcessoTodasFontes {

    private final ProcessoService processoService;
    private final ProcessoRemotoService processoRemotoService;
    private final List<FonteDados> fonteDados;

    private static final Logger logger = LoggerFactory.getLogger(BuscaProcessoTodasFontes.class);

    @Autowired
    public BuscaProcessoTodasFontes(ProcessoService processoService,
                                    ProcessoRemotoService processoRemotoService, List<FonteDados> fonteDados){
        this.processoService = processoService;
        this.processoRemotoService = processoRemotoService;
        this.fonteDados = fonteDados;
    }

    public Optional<ProcessoConcluso> findByNumeroRemoto(String numeroProcesso){
        for ( FonteDados fonte : fonteDados) {
            logger.info("Buscando processo por numero ["+numeroProcesso+"] na fonte "+fonte.getFonteDadosEnum().toString());
            Optional<ProcessoConcluso> processoConcluso = processoRemotoService.findByNumero(numeroProcesso, fonte);
            if(processoConcluso.isPresent())
                return processoConcluso;
        }
        return Optional.empty();
    }
}
