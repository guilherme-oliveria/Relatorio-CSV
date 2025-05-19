package br.jus.tjro.gabinete.importacao.builder;

import br.jus.tjro.gabinete.builders.service.BuilderProcessoParteService;
import br.jus.tjro.gabinete.scheduled.BuscaPartesDosProcessos;
import br.jus.tjro.gabinete.service.local.ProcessoParteService;

public class BuilderBuscaPartesDosProcessos {

    public BuscaPartesDosProcessos get() throws Exception {
        BuilderProcessoParteService builderProcessoParteService = new BuilderProcessoParteService();
        BuscaPartesDosProcessos buscaPartesProcessos = new BuscaPartesDosProcessos(builderProcessoParteService.get());
        return buscaPartesProcessos;
    }
}
