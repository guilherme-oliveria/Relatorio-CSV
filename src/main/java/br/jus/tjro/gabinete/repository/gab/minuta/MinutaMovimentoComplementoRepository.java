package br.jus.tjro.gabinete.repository.gab.minuta;

import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimentoComplemento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MinutaMovimentoComplementoRepository extends JpaRepository<MinutaMovimentoComplemento, Long> {

    List<MinutaMovimentoComplemento> findByMinutaMovimentoId(Long minutaMovimentoId);
}
