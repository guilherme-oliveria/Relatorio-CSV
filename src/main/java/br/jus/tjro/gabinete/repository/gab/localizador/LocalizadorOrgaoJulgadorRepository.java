package br.jus.tjro.gabinete.repository.gab.localizador;

import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorOrgaoJulgador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface LocalizadorOrgaoJulgadorRepository extends JpaRepository<LocalizadorOrgaoJulgador, Long> {
    Optional<LocalizadorOrgaoJulgador> findById(String id);
}
