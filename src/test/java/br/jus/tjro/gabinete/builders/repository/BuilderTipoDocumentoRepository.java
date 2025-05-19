package br.jus.tjro.gabinete.builders.repository;

import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.repository.gab.TipoDocumentoRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BuilderTipoDocumentoRepository {

    public TipoDocumentoRepository get() {
        TipoDocumentoRepository repository = mock(TipoDocumentoRepository.class);
        when(repository.findAll()).thenReturn(getTiposDocs());
        when(repository.findById(any())).thenReturn(java.util.Optional.ofNullable(getTiposDocs().get(0)));
        return repository;
    }

    private List<TipoDocumento> getTiposDocs(){
        TipoDocumento
            ob = new TipoDocumento("1"),
            ob2 = new TipoDocumento("2");
        return List.of(ob, ob2);
    }
}
