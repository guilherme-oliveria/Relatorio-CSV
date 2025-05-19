package br.jus.tjro.gabinete.repository.remoto;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinuta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ModeloDocumentoRepository {

    @PreAuthorize("canEditOrDeleteModeloDocumento(#modeloMinuta)")
    ModeloMinuta save(ModeloMinuta modeloMinuta,Usuario usuario) throws Exception;

    Optional<ModeloMinuta> findById(String id, Usuario usuario) throws Exception;

    Map<String, Long> totais(List<QueryModeloDocumento> querys, Usuario usuario) throws Exception;

    Page<ModeloMinuta> findBy(QueryModeloDocumento query, Pageable pageable,Usuario usuario) throws ServicoRemotoException;

    void delete(String id, Usuario usuario) throws ServicoRemotoException;
}
