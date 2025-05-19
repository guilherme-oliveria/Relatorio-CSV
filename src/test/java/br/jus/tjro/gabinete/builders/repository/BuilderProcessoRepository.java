package br.jus.tjro.gabinete.builders.repository;

import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;

import static org.mockito.Mockito.mock;

public class BuilderProcessoRepository {

    public ProcessosRepository get() {
        ProcessosRepository repository = mock(ProcessosRepository.class);
        return repository;
    }
}
