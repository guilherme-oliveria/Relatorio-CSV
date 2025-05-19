package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.repository.gab.endereco.EstadoRepository;
import br.jus.tjro.gabinete.service.local.EstadoService;

import static org.mockito.Mockito.mock;


public class BuilderEstadoService {

    public EstadoService get(){
        EstadoService service = new EstadoService(getEstadoRepository());
        return service;
    }

    private EstadoRepository getEstadoRepository() {
        EstadoRepository repository = mock(EstadoRepository.class);
        return repository;
    }
}
