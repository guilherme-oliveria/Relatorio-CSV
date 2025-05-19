package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.builders.repository.BuilderPessoaRepository;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaRepository;
import br.jus.tjro.gabinete.service.local.PessoaService;

public class BuilderPessoaService {


    public PessoaService get() {
        PessoaService service = new PessoaService(getPessoaRepository());
        return service;
    }

    public PessoaRepository getPessoaRepository() {
        BuilderPessoaRepository builder = new BuilderPessoaRepository();
        return builder.get();
    }
}
