package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.MovimentoFavorito;
import br.jus.tjro.gabinete.model.gab.minuta.MovimentoFavoritoPK;
import br.jus.tjro.gabinete.repository.gab.minuta.MovimentoFavoritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class MovimentoFavoritoService {

    @Autowired
    MovimentoFavoritoRepository movimentoFavoritoRepository;

    public List<Long> pegaIdsMovimentosFavoritosDoUsuario(String loginUsuario) {
        return movimentoFavoritoRepository.obtemIdsMovimentosFavoritosDoUsuario(loginUsuario);
    }

    @Transactional
    public void favoritarMovimento(Usuario usuario, Long idMovimento) {
        MovimentoFavoritoPK movimentoFavoritoPK = new MovimentoFavoritoPK(usuario, idMovimento);
        MovimentoFavorito movimentoFavorito = new MovimentoFavorito(movimentoFavoritoPK);
        movimentoFavoritoRepository.save(movimentoFavorito);
    }

    public void desfavoritarMovimento(Usuario usuario, Long idMovimento) {
        MovimentoFavorito movimentoFavorito = movimentoFavoritoRepository.obtemPorLoginEIdMovimento(usuario.getId(),
            idMovimento);
        movimentoFavoritoRepository.delete(movimentoFavorito);
    }
}
