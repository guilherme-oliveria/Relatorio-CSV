package br.jus.tjro.gabinete.importacao.builder;

import br.jus.tjro.gabinete.builders.service.BuilderProcessoParteService;
import br.jus.tjro.gabinete.scheduled.BuscaEnderecosDasPartesDosProcessos;
import br.jus.tjro.gabinete.service.local.ProcessoParteService;

public class BuilderBuscaEnderecosDasPartesDosProcessos {

    public BuscaEnderecosDasPartesDosProcessos get() throws Exception {
        BuscaEnderecosDasPartesDosProcessos build = new BuscaEnderecosDasPartesDosProcessos(getProcessoParteService());
        return build;
    }

    private ProcessoParteService getProcessoParteService() throws Exception {
        BuilderProcessoParteService builder = new BuilderProcessoParteService();
        return builder.get();
    }
}
