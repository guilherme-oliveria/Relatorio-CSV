package br.jus.tjro.gabinete.builders.repository;

import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BuilderPessoaRepository {

    public PessoaRepository get() {
        PessoaRepository repository = mock(PessoaRepository.class);
        List<Pessoa> listaPessoa = new ArrayList<>();
        when(repository.findByIdPessoaLegadoInAndSistema(anyList(),any())).thenReturn(listaPessoa);
        return repository;
    }
}
