package br.jus.tjro.gabinete.controller.processo;


import br.jus.tjro.gabinete.model.gab.minuta.Anotacao;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.AnotacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anotacao")
public class AnotacaoController {


    private final AnotacaoService anotacaoService;

    private final AuthenticationUsuarioService auth;

    @Autowired
    public AnotacaoController(AnotacaoService anotacao, AuthenticationUsuarioService auth){
        this.anotacaoService = anotacao;
        this.auth = auth;
    }

    @GetMapping("/{idProcesso}")
    public ResponseEntity<List<Anotacao>> obtemListaAnotacoesProcessoPorUsuario(@PathVariable("idProcesso") Long idProcesso, Authentication user) throws Exception {
        Usuario userAuth = auth.getUsuario(user);
        List<Anotacao> anotacoes = anotacaoService.obtemListaDeAnotacoes(idProcesso, userAuth.getCpf());
        return anotacoes.size() > 0 ? ResponseEntity.ok(anotacoes) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<List<Anotacao>> salvaAnotacaoProcesso(@RequestBody Anotacao anotacao, Authentication user) throws Exception {
        anotacaoService.salvaAnotacaoProcesso(anotacao, user);
        return this.obtemListaAnotacoesProcessoPorUsuario(anotacao.getIdProcesso(), user);
    }

    @GetMapping("/excluir/{idProcesso}/{anotacaoId}")
    public ResponseEntity<List<Anotacao>> excluirAnotacaoProcesso(@PathVariable("idProcesso") Long idProcesso, @PathVariable("anotacaoId") Long anotacaoId, Authentication user) throws Exception {
        anotacaoService.excluiAnotacaoProcesso(anotacaoId);
        return obtemListaAnotacoesProcessoPorUsuario(idProcesso, user);
    }
}
