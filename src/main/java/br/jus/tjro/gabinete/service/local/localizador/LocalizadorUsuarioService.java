package br.jus.tjro.gabinete.service.local.localizador;

import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorUsuario;
import br.jus.tjro.gabinete.repository.gab.localizador.LocalizadorUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


@Service
public class LocalizadorUsuarioService {

    private final LocalizadorUsuarioRepository repository;

    @Autowired
    public LocalizadorUsuarioService(LocalizadorUsuarioRepository repository){
        this.repository = repository;
    }

    public void verificaSeExisteLocUsuarioEhSalva(Set<LocalizadorUsuario> usuarios) {
        usuarios.stream().forEach(user ->{
            repository.findOne(Example.of(user)).or(
                () -> {return Optional.of(repository.save(user));}
            );
        });
    }
}


