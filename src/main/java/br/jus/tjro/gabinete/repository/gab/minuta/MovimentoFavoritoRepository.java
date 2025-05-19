package br.jus.tjro.gabinete.repository.gab.minuta;

import br.jus.tjro.gabinete.model.gab.minuta.MovimentoFavorito;
import br.jus.tjro.gabinete.model.gab.minuta.MovimentoFavoritoPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import java.util.List;

@Transactional
public interface MovimentoFavoritoRepository extends JpaRepository<MovimentoFavorito, MovimentoFavoritoPK> {

    @Query("SELECT MF.id.codigoMovimento "
        + "FROM MovimentoFavorito MF "
        + "WHERE MF.id.loginUsuario = :loginUsuario")
    List<Long> obtemIdsMovimentosFavoritosDoUsuario(@Param("loginUsuario") String loginUsuario);


    @Query("SELECT MF "
        + "FROM MovimentoFavorito MF "
        + "where MF.id.loginUsuario = :usuario "
        + "and MF.id.codigoMovimento = :idMovimento  ")
    MovimentoFavorito obtemPorLoginEIdMovimento(@Param("usuario") String usuario, @Param("idMovimento") Long idMovimento);

}
