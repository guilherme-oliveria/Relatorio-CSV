package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteEndereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ParteEnderecoRepository extends CrudRepository<ProcessoParteEndereco, Long> {

    Page<ProcessoParteEndereco> findByProcessoParte(@Param("idProcessoParte") ProcessoParte ProcessoParte, Pageable pageable);

}
