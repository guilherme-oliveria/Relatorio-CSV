package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ProcessoTagRepository extends JpaRepository<ProcessoTag, Long> {

    List<ProcessoTag> findByProcesso(Processo processo);

    List<ProcessoTag> findByProcessoId(Long processoId);

    List<ProcessoTag> findByTag_Id(Long tagId);

    List<ProcessoTag> findAllByProcessoAndTag(Processo processo, Tag tag);

    List<ProcessoTag> findAllByTag(Tag tag);

    ProcessoTag findByProcessoAndTag(Processo processo, Tag tag);

    @Modifying
    @Transactional
    @Query(value = "update processo_tag set data_exclusao = CURRENT_TIMESTAMP, usuario_exclusao = :usuario where id_tag = :#{#tag.getId()} and id_processo = :#{#processo.getId()}", nativeQuery = true)
    Integer deleteUpdate(@Param("processo") Processo processo,@Param("tag") Tag tag, @Param("usuario") String usuario);

    @Query("SELECT pt FROM ProcessoTag pt LEFT JOIN FETCH pt.tag ta WHERE pt.processo.id = :processoId AND (pt.dataExclusao IS NULL OR pt.dataExclusao > :data) AND ta.dataExclusao IS NULL")
    List<ProcessoTag> findByProcessoTagAll(@Param("processoId") Long processoId, @Param("data") Date data);

}
