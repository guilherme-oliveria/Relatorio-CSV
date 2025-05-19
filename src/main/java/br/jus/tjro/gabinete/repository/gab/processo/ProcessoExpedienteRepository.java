package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.model.gab.processo.ProcessoExpediente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessoExpedienteRepository extends CrudRepository<ProcessoExpediente, Long> {

    Optional<ProcessoExpediente> findByIdLegado(Long idLegado);
}
