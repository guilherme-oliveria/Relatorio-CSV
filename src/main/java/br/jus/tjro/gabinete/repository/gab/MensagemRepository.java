package br.jus.tjro.gabinete.repository.gab;

import br.jus.tjro.gabinete.model.gab.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    @Query("SELECT m from Mensagem m where m.usuarioId = :idUsuario or m.usuarioId = NULL")
    List<Mensagem> findByUsuario(@Param("idUsuario") String idUsuario);

    @Query("SELECT m from Mensagem m where (m.usuarioId = :idUsuario or m.usuarioId = NULL) AND (m.enviado=false or m.enviado = NULL)")
    List<Mensagem> findByParaEnvio(@Param("idUsuario") String idUsuario);
}
