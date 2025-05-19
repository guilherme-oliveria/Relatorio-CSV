package br.jus.tjro.gabinete.repository.gab.minuta;

import br.jus.tjro.gabinete.model.gab.minuta.AutoTexto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import java.util.List;

@Transactional
public interface AutoTextoRepository extends JpaRepository<AutoTexto, Long> {
    List<AutoTexto> findByIdUsuarioAndDataExclusaoIsNullOrderByTitulo(String idUsuario);

    List<AutoTexto> findAllByIdOrgaoJulgadorAndIdUsuarioIsNotAndDataExclusaoIsNullOrderByTitulo(String oj, String idUsuario);
}
