package br.jus.tjro.gabinete.repository.gab.minuta;

import br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebida;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebidaStatus;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface MinutasRecebidasRepository extends CrudRepository<MinutaRecebida, Long> {

    @PostAuthorize("validaMinutaRecebidaAcessada(returnObject)")
    public List<MinutaRecebida> findByProcessoAndStatus(Processo processo, MinutaRecebidaStatus status);

}
