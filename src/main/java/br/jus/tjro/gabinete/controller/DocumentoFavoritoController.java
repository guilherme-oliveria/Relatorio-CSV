package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.model.gab.processo.DocumentoFavorito;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.DocumentoFavoritoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("documento-favorito")
public class DocumentoFavoritoController {


    private final DocumentoFavoritoService documentoFavoritoService;

    private final AuthenticationUsuarioService auth;

    @Autowired
    public DocumentoFavoritoController(AuthenticationUsuarioService auth, DocumentoFavoritoService documentoFavoritoService){
        this.auth = auth;
        this.documentoFavoritoService = documentoFavoritoService;
    }
    
    private final Logger looger = LoggerFactory.getLogger(DocumentoFavoritoController.class);

    @PostMapping("/{idDocumento}")
    public ResponseEntity<DocumentoFavorito> salvarDocumentoFavorito(
        @RequestBody DocumentoFavorito documentoFavorito,
        @PathVariable("idDocumento") Long idDocumento,
        Authentication user) {
        Usuario usuario = auth.getUsuario(user);
        documentoFavorito.setIdUsuario(usuario.getId());
        DocumentoFavorito docFavoritoSalvo = documentoFavoritoService.salvar(documentoFavorito, idDocumento);
        return docFavoritoSalvo != null ? ResponseEntity.ok(docFavoritoSalvo) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{idProcesso}")
    public ResponseEntity<List<DocumentoFavorito>> getDocumentosFavoritos(@PathVariable("idProcesso") Long idProcesso) {
        List<DocumentoFavorito> documentosFavoritadosParaOProcesso = documentoFavoritoService.buscaDocumetosFavoritoPorProcesso(idProcesso);
        return ResponseEntity.ok(documentosFavoritadosParaOProcesso);
    }

    @GetMapping("/{idProcesso}/{idDocumento}")
    public ResponseEntity<DocumentoFavorito> getDocumentoFavorito(@PathVariable("idProcesso") Long idProcesso, @PathVariable("idDocumento") Long idDocumento) {
        DocumentoFavorito documentoFavoritado = documentoFavoritoService.buscaDocumetosFavoritoPorProcesso(idProcesso, idDocumento);
        return ResponseEntity.ok(documentoFavoritado);
    }

    @PostMapping("/desfavoritar")
    public ResponseEntity<?> desfavoritarDocumento(@RequestBody Long idDocumentoFavorito) {
        try {
            documentoFavoritoService.desfavoritarDocumento(idDocumentoFavorito);
        } catch (Exception e) {
            looger.error(e.getMessage(),e);
            return ResponseEntity.noContent().header("error", e.getMessage()).build();
        }
        return ResponseEntity.ok(true);
    }
}
