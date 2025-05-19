package br.jus.tjro.gabinete.repository.gab.localizador;

import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface LocalizadorUsuarioRepository extends JpaRepository<LocalizadorUsuario, String> {

}
