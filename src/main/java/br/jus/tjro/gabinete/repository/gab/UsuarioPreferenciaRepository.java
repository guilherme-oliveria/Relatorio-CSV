package br.jus.tjro.gabinete.repository.gab;

import br.jus.tjro.gabinete.model.gab.UsuarioPreferencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioPreferenciaRepository extends JpaRepository<UsuarioPreferencia, Long> {
    List<UsuarioPreferencia> findAllByIdUsuario(String idUsuario);

    UsuarioPreferencia findByIdUsuarioAndPreferencia(String id, String preferencia);
}
