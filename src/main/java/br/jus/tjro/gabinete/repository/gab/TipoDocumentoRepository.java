package br.jus.tjro.gabinete.repository.gab;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;

import java.util.List;
import java.util.Optional;


public interface TipoDocumentoRepository {

    List<TipoDocumento> findAll();

    List<TipoDocumento> findAll(String ids) throws ServicoRemotoException;

    Optional<TipoDocumento> findById(String id);

}
