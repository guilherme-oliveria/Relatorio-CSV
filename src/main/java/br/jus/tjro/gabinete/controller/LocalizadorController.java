package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorAgrupador;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorCaixa;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorWrapper;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.CaixaService;
import br.jus.tjro.gabinete.service.local.TarefaService;
import br.jus.tjro.gabinete.service.local.localizador.LocalizadorService;
import br.jus.tjro.gabinete.service.local.localizador.LocalizadorWrapperService;
import br.jus.tjro.gabinete.service.remoto.LocalizadorRemotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("localizador")
public class LocalizadorController {

    @Autowired
    private LocalizadorRemotoService localizadorRemotoService;

    @Autowired
    private CaixaService caixaService;

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private LocalizadorWrapperService localizadorWrapperService;

    @Autowired
    private LocalizadorService localizadorService;

    @Autowired
    private AuthenticationUsuarioService auth;

    private final Logger looger = LoggerFactory.getLogger(LocalizadorController.class);

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public ResponseEntity<LocalizadorCaixa> salvar(@RequestBody LocalizadorWrapper localizadorWrapper, Authentication user) throws Exception {
        LocalizadorCaixa localizadorCaixa = localizadorWrapperService.wrapperForLocalizadorCaixa(localizadorWrapper, auth.getUsuario(user));
        return ResponseEntity.ok().body(localizadorService.salvaLocalizador(localizadorCaixa));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<LocalizadorCaixa> buscarPorId(@PathVariable("id") Long id) throws Exception {
        try {
            LocalizadorCaixa localizador = localizadorService.findById(id);
            return ResponseEntity.ok().body(localizador);
        } catch (Exception e) {
            looger.error(e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("error", e.getMessage()).body(null);
        }

    }

    @RequestMapping(value = "/buscarporusuario", method = RequestMethod.GET)
    public ResponseEntity<List<LocalizadorCaixa>> buscarPorUsuario(@RequestHeader("oj") String orgaojulgador, Authentication user) throws Exception {
        List<LocalizadorCaixa> localizador = localizadorService.findByUsuarioIdAndOrgaoJulgador(auth.getUsuario(user), orgaojulgador);
        return ResponseEntity.ok().body(localizador);
    }


    @RequestMapping(value = "/caixas", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public ResponseEntity<List<LocalizadorAgrupador>> caixas(
        @RequestHeader("oj") String orgaojulgador,
        Authentication user) throws Exception {
        Usuario usuario = auth.getUsuario(user);
        List<OrgaoJulgador> orgaojulgadores = getOrgaoJulgador(orgaojulgador,usuario);
        List<Caixa> caixas = caixaService.listaCaixasAgrupadas(orgaojulgadores);
        return ResponseEntity.ok().body(Caixa.getLocalizadoresAgrupador(caixas));
    }

    @RequestMapping(value = "/localizador/{manifestacao}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public ResponseEntity<List<LocalizadorAgrupador>> buscarPorUsuarioTarefaAgrupadoRemoto(
        @RequestHeader("oj") String orgaojulgador,
        @PathVariable("manifestacao") Long manifestacao,
        Authentication user) throws Exception {
        Usuario usuario = auth.getUsuario(user);
        List<LocalizadorCaixa> localizadores = localizadorService.findByUsuario(usuario, orgaojulgador)
            .stream()
            .filter(it -> it.isManifestacao(manifestacao))
            .collect(toList());
        List<LocalizadorAgrupador> localizadorAgruapador = localizadorRemotoService.buscaLocalizadorAgrupador(localizadores);
        return ResponseEntity.ok().body(localizadorAgruapador);
    }

    private List<OrgaoJulgador> getOrgaoJulgador(String orgaojulgador, Usuario usuario){
        List<OrgaoJulgador> orgaojulgadores;
        if(orgaojulgador.equals("-1") )
            orgaojulgadores = usuario.getOrgaosJulgadores().stream().map(i -> new OrgaoJulgador(i)).collect(toList());
        else
            orgaojulgadores = List.of(new OrgaoJulgador(orgaojulgador));
        return orgaojulgadores;
    }

    @RequestMapping(value = "/excluir", method = RequestMethod.POST)
    public ResponseEntity<Boolean> excluir(@RequestBody LocalizadorWrapper localizadorWrapper, Authentication user) throws Exception {
        localizadorService.excluir(localizadorWrapper.getId(), localizadorWrapper.getOrgaoJulgador(), auth.getUsuario(user));
        return ResponseEntity.ok().body(true);

    }
}
