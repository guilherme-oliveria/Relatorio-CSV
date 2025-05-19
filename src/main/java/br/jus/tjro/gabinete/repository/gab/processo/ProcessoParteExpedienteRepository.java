package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteExpediente;
import br.jus.tjro.gabinete.repository.gab.processo.helper.ProcessoParteExpedienteRepositoryQueries;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessoParteExpedienteRepository extends CrudRepository<ProcessoParteExpediente, Long>, ProcessoParteExpedienteRepositoryQueries {
    @Override
    void deleteAll(Iterable<? extends ProcessoParteExpediente> entities);

    Optional<ProcessoParteExpediente> findByIdLegado(Long idLegado);

    @Query(value = "select ppe from ProcessoParteExpediente  ppe join ppe.processoExpediente pe join pe.processo p where p.id = :idProcesso")
    List<ProcessoParteExpediente> findAllByProcesso(@Param("idProcesso") Long idProcesso);

}
