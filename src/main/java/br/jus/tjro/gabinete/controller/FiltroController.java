package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.localizador.Filtro;
import br.jus.tjro.gabinete.model.gab.localizador.FiltroWrapper;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.localizador.FiltroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("filtro")
public class FiltroController {

    private final FiltroService filtroService;

    private final AuthenticationUsuarioService auth;

    @Autowired
    public FiltroController(FiltroService filtroService, AuthenticationUsuarioService auth){
        this.filtroService = filtroService;
        this.auth = auth;
    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ResponseEntity<Object> salvar(@RequestBody FiltroWrapper filtro, Authentication user) throws Exception {
        Usuario usuario = auth.getUsuario(user);
        if (filtro.validar()) {
            Filtro filt = filtroService.salvar(filtro, usuario);
            filtro.setId(filt.getId());
        } else {
            throw new Exception("Filtro não é válido");
        }
        return ResponseEntity.ok().body(filtro);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Filtro> buscarPorId(@PathVariable("id") Long id, Authentication user) throws Exception {
        Filtro filtro = filtroService.findById(id);
        return ResponseEntity.ok().body(filtro);
    }

    @RequestMapping(value = "/buscarporusuario/{orgaojulgador}", method = RequestMethod.GET)
    public ResponseEntity<List<Filtro>> buscarPorUsuario(@PathVariable("orgaojulgador") String orgaojulgador, Authentication user) throws Exception {
        Usuario usuario = auth.getUsuario(user);
        List<Filtro> filtro = filtroService.findByUsuarioIdAndOrgaoJulgador(usuario.getId(), orgaojulgador);
        return ResponseEntity.ok().body(filtro);
    }

    @RequestMapping(value = "/excluir", method = RequestMethod.POST)
    public ResponseEntity<Boolean> excluir(@RequestBody FiltroWrapper filtroWrapper, Authentication user) throws Exception {
        Usuario usuario = auth.getUsuario(user);
        filtroService.excluir(filtroWrapper.getId(), filtroWrapper.getOrgaoJulgador(), usuario);
        return ResponseEntity.ok().body(true);

    }

}



