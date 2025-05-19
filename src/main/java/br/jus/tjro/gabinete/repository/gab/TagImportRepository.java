package br.jus.tjro.gabinete.repository.gab;

import br.jus.tjro.gabinete.model.gab.tag.TagImport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagImportRepository extends JpaRepository<TagImport, Long> {

    Optional<TagImport> findByNrProcessoAndNomeTag(String nrProcesso, String nomeTag);

    Optional<TagImport> findByNrOabAndSiglaEstadoAndNomeTag(String nrOab,String siglaEstado,  String nomeTag);

    List<TagImport> findByNrProcesso(String nrProcesso);

    List<TagImport> findByNrOabAndSiglaEstado(String nrOab, String siglaEstado);

    void deleteByNrProcessoAndNomeTag(String nrProcesso, String nomeTag);

    void deleteByNrOabAndSiglaEstadoAndNomeTag(String nrOab, String siglaEstado, String nomeTag);

    Page<TagImport> findAll(Pageable pageable);


}
