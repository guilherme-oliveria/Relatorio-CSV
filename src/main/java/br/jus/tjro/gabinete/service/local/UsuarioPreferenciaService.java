package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.UsuarioPreferencia;
import br.jus.tjro.gabinete.repository.gab.UsuarioPreferenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioPreferenciaService {
    @Autowired
    UsuarioPreferenciaRepository usuarioPreferenciaRepository;

    public List<UsuarioPreferencia> pegaTodosDoUsuario(Usuario usuario) {
        return this.usuarioPreferenciaRepository.findAllByIdUsuario(usuario.getId());
    }

    public UsuarioPreferencia salvar(Usuario usuario, UsuarioPreferencia preferencia) {
        preferencia.setIdUsuario(usuario.getId());
        UsuarioPreferencia preferenciaAtual = preferencia;
        preferenciaAtual.setValor(preferencia.getValor());
        return this.usuarioPreferenciaRepository.save(preferenciaAtual);
    }

    public UsuarioPreferencia pegaPreferenciaDoUsuarioPorChave(Usuario usuario, String preferencia) {
        return this.usuarioPreferenciaRepository.findByIdUsuarioAndPreferencia(usuario.getId(), preferencia);
    }
}
