package br.jus.tjro.gabinete.repository.gab.minuta;

import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;
import java.util.List;

@Transactional
public interface MinutaMovimentoRepository extends JpaRepository<MinutaMovimento, Long> {

    List<MinutaMovimento> findByMinutaIdAndMovimentoId(Long minutaId, Long movimentoId);

    List<MinutaMovimento> findByMinuta(Minuta minuta);

    List<MinutaMovimento> getAllByMinuta(Minuta minuta);

    void deleteAllByMinuta(Minuta minuta);
}
