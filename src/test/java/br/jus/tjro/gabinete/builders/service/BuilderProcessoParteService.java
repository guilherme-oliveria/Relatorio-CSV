package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.builders.repository.BuilderPessoaRepository;
import br.jus.tjro.gabinete.builders.repository.BuilderProcessoParteRepository;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoParteRepository;
import br.jus.tjro.gabinete.service.local.ProcessoParteService;
import br.jus.tjro.gabinete.service.remoto.ParteRemotoService;


public class BuilderProcessoParteService {

    public ProcessoParteService get() throws Exception {
        ProcessoParteService processoparteService = new ProcessoParteService(
            getParteRemotoService(),
            getProcessoParteRepository(),
            getPessoaRepository());
        return processoparteService;
    }

    public ParteRemotoService getParteRemotoService() throws Exception {
        BuilderParteRemotoService builder = new BuilderParteRemotoService();
        return builder.get();
    }

    public ProcessoParteRepository getProcessoParteRepository() {
        BuilderProcessoParteRepository builder = new BuilderProcessoParteRepository();
        return builder.get();
    }

    public PessoaRepository getPessoaRepository() {
        BuilderPessoaRepository builder = new BuilderPessoaRepository();
        return builder.get();
    }
}
