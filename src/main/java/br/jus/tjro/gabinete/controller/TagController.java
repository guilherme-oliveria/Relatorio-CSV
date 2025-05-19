package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.exceptions.CabecalhoInvalidoException;
import br.jus.tjro.gabinete.exceptions.InvalidFileTypeException;
import br.jus.tjro.gabinete.exceptions.service.local.CampoInvalidoException;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService;
import br.jus.tjro.gabinete.service.local.ProcessoTagService;
import br.jus.tjro.gabinete.service.local.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.List;

@RestController
@RequestMapping("tag")
public class TagController {

    @Value("${invalidFileType.message}")
    private String invalidFileType;
    private final TagService tagService;

    private final ProcessoTagService processoTagService;

    private final AuthenticationUsuarioService auth;

    @Autowired
    public TagController(TagService tagService, AuthenticationUsuarioService auth, ProcessoTagService processoTagService){
        this.auth = auth;
        this.tagService = tagService;
        this.processoTagService=processoTagService;
    }

    @GetMapping("/")
    public List<Tag> ObterPorOrgaoJulgador(@RequestHeader("oj") String idOJ, Authentication authentication) throws Exception  {
        Usuario usuarioLogado = auth.getUsuario(authentication);
        return tagService.obterPorOrgaoJulgador(idOJ, usuarioLogado);
    }

    @GetMapping("/{id}")
    public Tag ObterPorOrgaoJulgadorEId(@RequestHeader("oj") String idOJ, @PathVariable("id") Long id) throws Exception  {
        return tagService.obterPorOrgaoJulgadorEId(idOJ, id);
    }

    @PostMapping("/")
    public Tag salvar(@RequestBody Tag tag, @RequestHeader("oj") String idOJ) throws Exception {
        tag.setOrgaoJulgador(idOJ);
        tag.setTag(tag.getTag().trim());
        return tagService.verificarSeExisteESalvar(tag);
    }

    @DeleteMapping("/")
    public ResponseEntity excluir(@RequestBody Tag tag, Authentication user) throws Exception {
        tagService.delete(tag.getId(),auth.getUsuario(user));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/{tipoUpload}/file", consumes = {"multipart/form-data;"})
    public ResponseEntity<Object> uploadAnexo(@PathVariable("tipoUpload") int tipoAcao, @RequestBody MultipartFile file, Authentication user) throws Exception {
        if (!StringUtils.getFilenameExtension(file.getOriginalFilename()).equalsIgnoreCase("csv")) {
            throw new InvalidFileTypeException(invalidFileType);
        }
        try {
            InputStreamResource resourceError = processoTagService.importarTagCsv(tipoAcao, file.getInputStream(),auth.getUsuario(user));

            if(resourceError!= null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.parseMediaType("text/plain; charset=iso-8859-1")).body(resourceError);
            }else{
                return ResponseEntity.ok().body(true);
            }
        } catch (CabecalhoInvalidoException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (CampoInvalidoException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    @GetMapping("/geral-csv")
    public ResponseEntity<InputStreamResource> recuperaCsvExemplo() throws Exception {
        InputStreamResource resource = processoTagService.buscaTodaTagImport();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exemplo-tags.zip");
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.parseMediaType("application/zip"))
            .body(resource);    }
}
