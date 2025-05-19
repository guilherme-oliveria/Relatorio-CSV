package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.dto.PapelDTO;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.webjud.OrgaosJulgadoresRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.OrgaoJulgadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("orgao-julgador")
public class OrgaoJulgadorController {

    private final OrgaoJulgadorService orgaoJulgadorService;
    private final OrgaosJulgadoresRepository repository;

    private final AuthenticationUsuarioService auth;

    @Autowired
    public OrgaoJulgadorController(AuthenticationUsuarioService auth,
                                   OrgaoJulgadorService orgaoJulgadorService,
                                   OrgaosJulgadoresRepository repository){
        this.orgaoJulgadorService = orgaoJulgadorService;
        this.auth = auth;
        this.repository = repository;
    }

    @GetMapping("/")
    public ResponseEntity<List<OrgaoJulgador>> pegaOrgaosJulgadoresDoUsuario(Authentication user) {
        Usuario usuario = auth.getUsuario(user);
        return ResponseEntity.ok(usuario.getOrgaosJulgadoresCompleto());
    }

    @GetMapping()
    public ResponseEntity<List<OrgaoJulgador>> getOrgaoJulgadores(Authentication user) {
        return pegaOrgaosJulgadoresDoUsuario(user);
    }

    @GetMapping("/{idOj}")
    public ResponseEntity<OrgaoJulgador> pegaOrgaoJulgadorPeloId(@PathVariable("idOj") String idOj) {
        OrgaoJulgador orgaoJulgador = orgaoJulgadorService.pegaOrgaoJulgadorPorId(idOj);
        return orgaoJulgador != null ? ResponseEntity.ok(orgaoJulgador) : ResponseEntity.noContent().build();
    }


    @GetMapping("/usuario-limpa-cache/{idUsuario:.+}")
    public ResponseEntity<String> limpaCache(@PathVariable("idUsuario") String idUsuario) {
        Usuario usuario = new Usuario("", idUsuario, new ArrayList<>(), "");
        repository.evictOrgaoJulgadoresByUsuario(usuario);
        repository.evictPapeisByUsuario(usuario);
        return ResponseEntity.ok("Limpando cache do usuario "+idUsuario);
    }

    @GetMapping("/usuario-atualiza-cache/{idUsuario:.+}")
    public ResponseEntity<String> atualizaCache(@PathVariable("idUsuario") String idUsuario) {
        repository.atualizaOrgaoJulgadorByUsuario(new Usuario("", idUsuario, new ArrayList<>(), ""));
        return ResponseEntity.ok("Atualizando cache do usuario " + idUsuario);
    }

    @GetMapping("/perfis-usuario")
    public ResponseEntity<Map<String, List<PapelDTO>>> pegarPapeisDoUsuario(Authentication user) {
        return ResponseEntity.ok(auth.getUsuario(user).getAllPapeisDto());
    }
}
