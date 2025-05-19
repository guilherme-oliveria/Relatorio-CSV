package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.builders.repository.BuilderPessoaRepository;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaRepository;
import br.jus.tjro.gabinete.service.local.CaixaService;
import br.jus.tjro.gabinete.service.local.MinutaTarefaLogService;
import br.jus.tjro.gabinete.service.local.ProcessoService;

import static org.mockito.Mockito.mock;

public class BuilderProcessoService {


    public ProcessoService get() {
        ProcessoService service = new ProcessoService(null,null,mock(MinutaTarefaLogService.class),
            mock(CaixaService.class),"",null);
        return service;
    }

    public PessoaRepository getPessoaRepository() {
        BuilderPessoaRepository builder = new BuilderPessoaRepository();
        return builder.get();
    }
}
