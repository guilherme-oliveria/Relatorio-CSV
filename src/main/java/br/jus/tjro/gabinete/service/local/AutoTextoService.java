package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.AutoTexto;
import br.jus.tjro.gabinete.repository.gab.minuta.AutoTextoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AutoTextoService {

    @Autowired
    private AutoTextoRepository autoTextoRepository;

    public AutoTexto salvaAutoTexto(AutoTexto autoTexto) {
        return autoTextoRepository.save(autoTexto);
    }

    public List<AutoTexto> buscaTodosDoUsuario(Usuario usuario) {
        return autoTextoRepository.findByIdUsuarioAndDataExclusaoIsNullOrderByTitulo(usuario.getId());
    }

    public List<AutoTexto> buscaTodosDoOJExcetoDoUsuario(String oj, Usuario usuario) {
        return autoTextoRepository.findAllByIdOrgaoJulgadorAndIdUsuarioIsNotAndDataExclusaoIsNullOrderByTitulo(oj, usuario.getId());
    }

    public void exlcuir(AutoTexto autoTexto, Usuario usuario) {
        autoTexto.setDataExclusao(new Date());
        autoTexto.setIdUsuarioExclusao(usuario.getId());
        autoTextoRepository.save(autoTexto);
    }
}
