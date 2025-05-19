package br.jus.tjro.gabinete.service.local.localizador;

import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorOrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.localizador.LocalizadorOrgaoJulgadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


@Service
public class LocalizadorOrgaoJulgadorService {


    private final LocalizadorOrgaoJulgadorRepository repository;

    @Autowired
    public LocalizadorOrgaoJulgadorService(LocalizadorOrgaoJulgadorRepository repository){
        this.repository = repository;
    }


    public void verificaSeExisteLocOrgaoJulgadorEhSalva(Set<LocalizadorOrgaoJulgador> orgaosJulgadores) {
        orgaosJulgadores.stream().forEach(oj ->{
            repository.findOne(Example.of(oj)).or(
                () -> {return Optional.of(repository.save(oj));}
            );
        });
    }

    public Optional<LocalizadorOrgaoJulgador> findByOrgaoJulgadorId(String orgaoJulgadorId) {
        return repository.findById(orgaoJulgadorId);
    }
}


