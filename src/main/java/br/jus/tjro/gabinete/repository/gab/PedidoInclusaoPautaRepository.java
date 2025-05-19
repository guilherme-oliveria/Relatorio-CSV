package br.jus.tjro.gabinete.repository.gab;

import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.PedidoInclusaoPauta;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;

public interface PedidoInclusaoPautaRepository extends JpaRepository<PedidoInclusaoPauta, Long> {
    Optional<PedidoInclusaoPauta> findByMinuta(@NotNull(message = "Minuta não pode ser nulo") Minuta minuta);
}
