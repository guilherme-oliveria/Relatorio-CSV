package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.dto.PapelDTO;
import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.webjud.OrgaosJulgadoresRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrgaoJulgadorService {

    private final Logger looger = LoggerFactory.getLogger(OrgaoJulgadorService.class);

    @Autowired
    private OrgaosJulgadoresRepository orgaosJulgadoresRepository;

    @Deprecated
    public OrgaoJulgador pegaOrgaoJulgadorPorId(String id) {
        return orgaosJulgadoresRepository.findById(id).orElseThrow(NullPointerException::new); // ToDo tratar a falta desse recurso
    }

    public List<OrgaoJulgador> findAllByUsuario(Usuario usuario) {
        return orgaosJulgadoresRepository.findAllByUsuario(usuario);
    }

    public Map<String, List<Papel>> findAllPapelByUsuario(Usuario usuario) {
        return orgaosJulgadoresRepository.findAllPapelByUsuario(usuario);
    }
}
