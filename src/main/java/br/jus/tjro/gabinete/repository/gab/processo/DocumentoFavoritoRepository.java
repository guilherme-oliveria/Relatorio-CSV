package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.model.gab.processo.DocumentoFavorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentoFavoritoRepository extends JpaRepository<DocumentoFavorito, Long> {
    Optional<DocumentoFavorito> findById(Long idDocumentoFavorito);

    @Query("select df" +
        " FROM DocumentoFavorito df " +
        " JOIN df.documento d " +
        " where df.idProcesso = :idProcesso" +
        " order by d.dataJuntada desc ")
    List<DocumentoFavorito> findByIdProcessoOrderByDocumentoDataJuntadaDesc(@Param("idProcesso") Long idProcesso);

    @Query("select df" +
        " FROM DocumentoFavorito df " +
        " JOIN df.documento d " +
        " where df.idProcesso = :idProcesso" +
        " and df.documento.id = :idDocumento" +
        " order by d.dataJuntada desc ")
    DocumentoFavorito findByIdProcessoAndIdDocumento(@Param("idProcesso") Long idProcesso, @Param("idDocumento") Long idDocumento);
}
