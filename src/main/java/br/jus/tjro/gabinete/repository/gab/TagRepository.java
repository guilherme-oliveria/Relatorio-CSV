package br.jus.tjro.gabinete.repository.gab;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t WHERE LOWER(t.orgaoJulgador) = LOWER(:orgaoJulgador) ")
    List<Tag> findByOrgaoJulgador(String orgaoJulgador);

    @Query("SELECT t FROM Tag t WHERE LOWER(t.orgaoJulgador) IN (:orgaoJulgador) ")
    List<Tag> findByOrgaosJulgadores(List<String> orgaoJulgador);

    @Query("SELECT t FROM Tag t WHERE t.id = :id AND LOWER(t.orgaoJulgador) = LOWER(:orgaoJulgador) ")
    List<Tag> findByOrgaoJulgadorAndId(String orgaoJulgador, Long id);

    Tag findFirstByIdTagPje(Integer id);

    @Query("SELECT t FROM Tag t WHERE LOWER(t.tag) = LOWER(:nomeTag) AND LOWER(t.orgaoJulgador) = LOWER(:orgaoJulgador) ")
    List<Tag> findByTagAndOrgaoJulgador(String nomeTag, String orgaoJulgador);

    @Query("SELECT t FROM Tag t " +
        "WHERE t.apenasMeuGabinete IS NOT NULL AND LOWER(t.tag) = LOWER(:nomeTag) AND LOWER(t.orgaoJulgador) = LOWER(:orgaoJulgador) ")
    List<Tag> findByTagAndOrgaoJulgadorNotNull(String nomeTag, String orgaoJulgador);

    @Modifying
    @Transactional
    @Query(value = "update tag set data_exclusao = CURRENT_TIMESTAMP, usuario_exclusao = :#{#usuario.getId()} where id = :#{#tag.getId()}", nativeQuery = true)
    Integer delete(@Param("tag") Tag tag, @Param("usuario") Usuario usuario);

}
