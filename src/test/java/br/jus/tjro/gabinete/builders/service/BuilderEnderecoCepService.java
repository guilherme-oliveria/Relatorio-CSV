package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.repository.gab.endereco.EnderecoRepository;
import br.jus.tjro.gabinete.service.local.EnderecoCepService;

import static org.mockito.Mockito.mock;


public class BuilderEnderecoCepService {

    public EnderecoCepService get(){
        EnderecoCepService service = new EnderecoCepService();
        return service;
    }

    private EnderecoRepository getEnderecoRepository(){
        EnderecoRepository repository = mock(EnderecoRepository.class);
        return repository;
    }
}
