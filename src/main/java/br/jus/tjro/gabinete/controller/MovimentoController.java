package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.dto.MovimentoProcessoDTO;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.tpu.TpuMovimento;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.MovimentoFavoritoService;
import br.jus.tjro.gabinete.service.local.ProcessoMovimentoService;
import br.jus.tjro.gabinete.service.local.tpu.TpuMovimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("movimentos")
public class MovimentoController {

    @Autowired
    private MovimentoFavoritoService movimentoService;

    @Autowired
    private ProcessoMovimentoService processoMovimentoService;

    @Autowired
    private TpuMovimentoService tpuMovimentoService;

    @Autowired
    private AuthenticationUsuarioService auth;

    @GetMapping("/{id}")
    public ResponseEntity<TpuMovimento> movimento(@PathVariable("id") Long id) {
        TpuMovimento movimento = this.tpuMovimentoService.getMovimento(id);
        return ResponseEntity.ok().body(movimento);
    }

    @GetMapping("/arvore")
    public ResponseEntity<List<TpuMovimento>> movimentoEmArvore() throws Exception {
        return movimentoEmArvore(null);
    }

    @GetMapping("/arvore/{id}")
    public ResponseEntity<List<TpuMovimento>> movimentoEmArvore(@PathVariable Long id) throws Exception {
        List<TpuMovimento> movimentoArvore = tpuMovimentoService.getArvoreMovimentos(id);
        return movimentoArvore != null ? ResponseEntity.ok(movimentoArvore) : ResponseEntity.noContent().build();
    }

    @GetMapping("/processo")
    public Page<MovimentoProcessoDTO> movimentoProcesso(long idProcesso, Pageable pageable) throws Exception {
        return processoMovimentoService.pegaMovimentosDoProcessoPeloIdProcesso(idProcesso, pageable);
    }

    @GetMapping("/favoritos")
    public ResponseEntity<List<Long>> movimentosFavoritos(Authentication user) {
        Usuario usuario = auth.getUsuario(user);
        return ResponseEntity.ok(movimentoService.pegaIdsMovimentosFavoritosDoUsuario(usuario.getId()));
    }

    @GetMapping("/favoritar/{id_movimento}")
    public ResponseEntity<?> favoritarMovimento(Authentication user, @PathVariable("id_movimento") Long idMovimento) {
        movimentoService.favoritarMovimento(auth.getUsuario(user), idMovimento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/desfavoritar/{id_movimento}")
    public ResponseEntity<?> desfavoritarMovimento(Authentication user, @PathVariable("id_movimento") Long idMovimento) {
        movimentoService.desfavoritarMovimento(auth.getUsuario(user), idMovimento);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
