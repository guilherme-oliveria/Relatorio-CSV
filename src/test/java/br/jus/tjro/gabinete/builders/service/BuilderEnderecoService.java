package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.model.gab.endereco.Endereco;
import br.jus.tjro.gabinete.repository.gab.endereco.EnderecoRepository;
import br.jus.tjro.gabinete.service.local.EnderecoCepService;
import br.jus.tjro.gabinete.service.local.EnderecoService;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Random;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BuilderEnderecoService {

    public EnderecoService get(){
        EnderecoService service = new EnderecoService(getEnderecoRepository(),getEnderecoCepService());
        return service;
    }

    private EnderecoRepository getEnderecoRepository(){
        EnderecoRepository repository = mock(EnderecoRepository.class);
        when(repository.save(any(Endereco.class))).thenAnswer(
            (Answer<Endereco>) invocation -> {
                Endereco endereco = invocation.getArgument(0);
                endereco.setId(new Random().nextLong());
                return endereco;
            });
        return repository;
    }

    private EnderecoCepService getEnderecoCepService(){
        BuilderEnderecoCepService builder = new BuilderEnderecoCepService();
        EnderecoCepService service = builder.get();
        return service;
    }
}
