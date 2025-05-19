package br.jus.tjro.gabinete.repository.gab.assinatura;

import br.jus.tjro.gabinete.model.gab.minuta.CadeiaCertificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CadeiaCertificadoRepository extends JpaRepository<CadeiaCertificado, Long> {

    Optional<CadeiaCertificado> findByHashCadeiaCertificado(String hash);
}
