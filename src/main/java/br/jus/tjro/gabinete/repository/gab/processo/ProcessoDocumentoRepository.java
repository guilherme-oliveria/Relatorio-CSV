package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import com.google.common.primitives.Longs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;


@Repository
public interface ProcessoDocumentoRepository extends CrudRepository<ProcessoDocumento, Long> {

    List<ProcessoDocumento> findByProcesso(Processo processo);

    List<ProcessoDocumento> findByProcessoAndIdDocumentoSistemaLegadoIn(Processo processo, List<String> idsDocumentoSistemaLegado);

    Long countByProcesso(Processo processo);

    ProcessoDocumento findFirstByProcessoAndIdDocumentoSistemaLegado(Processo processo, String idDocumentoSistemaLegado);

    Page<ProcessoDocumento> findByProcesso(Processo processo, Pageable pageable);
}
