package br.jus.tjro.gabinete.controller.sessao;

import br.jus.tjro.gabinete.config.ColegiadoEnvironment;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.webjud.OrgaosJulgadoresRepository;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.MinutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("acordao")
public class AcordaoController {

    @Autowired
    private MinutaService minutaService;

    @Autowired
    private AuthenticationUsuarioService auth;

    @Autowired
    private OrgaosJulgadoresRepository orgaosJulgadoresRepository;

    @GetMapping("/minuta/{idMinuta}/relatorio")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<String> getHtmlRelatorio(@PathVariable Long idMinuta, Authentication authentication) throws Exception {
        return this.getHtml(idMinuta, ColegiadoEnvironment.ID_RELATORIO, authentication);
    }

    @GetMapping("/minuta/{idMinuta}/voto")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<String> getHtmlVoto(@PathVariable Long idMinuta, Authentication authentication) throws Exception {
        return this.getHtml(idMinuta, ColegiadoEnvironment.ID_VOTO, authentication);
    }

    @GetMapping("/minuta/{idMinuta}/ementa")
    @PreAuthorize("hasAuthority('MAGISTRADO')")
    public ResponseEntity<String> getHtmlEmenta(@PathVariable Long idMinuta, Authentication authentication) throws Exception {
        return this.getHtml(idMinuta, ColegiadoEnvironment.ID_EMENTA, authentication);
    }

    @GetMapping("/minuta/{idMinuta}/preliminar/{idPreliminar}")
    public ResponseEntity<String> getHtmlPreliminar(@PathVariable Long idMinuta, @PathVariable Long idPreliminar, Authentication authentication) throws Exception {
        Usuario usuario = auth.getUsuario(authentication);
        Minuta minuta = minutaService.findOne(idMinuta);

        this.validaAcesso(usuario, minuta.getProcesso());

        Optional<MinutaAnexo> preliminar = minuta.getPreliminares()
            .stream()
            .filter(prelim -> prelim.getId().equals(idPreliminar))
            .findFirst();

        return preliminar
            .map(prelim -> ResponseEntity.ok(prelim.getHtml()))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


    private ResponseEntity<String> getHtml(Long idMinuta, String idTipoDocumento, Authentication authentication) throws Exception {
        Usuario usuario = auth.getUsuario(authentication);
        Minuta minuta = minutaService.findOne(idMinuta);

        this.validaAcesso(usuario, minuta.getProcesso());

        Optional<MinutaAnexo> documento = minuta.getAnexos()
            .stream()
            .filter(anexo -> anexo.getTipoDocumento().getId().equals(idTipoDocumento))
            .findFirst();
        return documento
            .map(doc -> ResponseEntity.ok(doc.getHtml()))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public void validaAcesso(Usuario usuario, Processo processo) throws Exception {
        String idColegiado = processo.getIdColegiado()
            .orElseThrow(() -> new Exception("Processo " + processo.getNumeroProcesso() + " sem idColegiado"));

        var permissao = orgaosJulgadoresRepository.findAllByIdColegiado(idColegiado).stream()
            .anyMatch((oj) -> usuario.isMagistrado(oj) || usuario.isAssessor(oj));

        if (!permissao) {
            throw new Exception("Usuário sem permissão para acessar o documento");
        }
    }
}
