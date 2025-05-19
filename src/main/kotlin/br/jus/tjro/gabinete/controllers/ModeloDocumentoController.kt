package br.jus.tjro.gabinete.controllers

import br.jus.tjro.gabinete.interfaces.ModeloDocumentoService
import br.jus.tjro.gabinete.model.docs.Tag
import br.jus.tjro.gabinete.model.gab.TipoDocumento
import br.jus.tjro.gabinete.repositories.DocsTagService
import br.jus.tjro.gabinete.security.AuthenticationUsuarioService
import br.jus.tjro.gabinete.security.CustomMethodSecurityExpressionRoot.isUsuarioTemOrgaoJulgador
import br.jus.tjro.gabinete.service.local.TipoDocumentoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/modelo-documentos")
class ModeloDocumentoController(private val serviceDocs: DocsTagService,
                                private val tipoDocService: TipoDocumentoService,
                                private val authService: AuthenticationUsuarioService) {
    @Autowired
    lateinit var modeloDocumento: ModeloDocumentoService

    @GetMapping(value = ["/tipos-documentos"])
    fun tipoDocumentos(): ResponseEntity<List<TipoDocumento>> = ResponseEntity.ok().body(tipoDocService.pegaTodosMinuta())

    @GetMapping("/tags/{sistema}")
    fun search(@PathVariable sistema: String,
               @RequestParam(required = false, name = "orgaoJulgador") orgaoJulgador: String?,
               @RequestParam(required = false, name = "term") term: String?,
               auth: Authentication): ResponseEntity<Array<Tag>> =
        serviceDocs.findByDescricao(sistema, orgaoJulgador, term)

    @DeleteMapping("/tags/{modelo}/{tag}")
    fun `delete modelo`(@PathVariable modelo: String, @PathVariable tag: String): ResponseEntity<Void> =
        serviceDocs.delete(modelo, tag)

    @PutMapping("/tags")
    fun save(@RequestBody tag: Tag): ResponseEntity<Tag> = serviceDocs.save(tag)

    @RequestMapping("/tags", method = [RequestMethod.HEAD])
    fun canSave(): ResponseEntity<Void> = ResponseEntity(ACCEPTED)

    @RequestMapping(value = ["/tags/permission/{oj}"], method = [RequestMethod.HEAD])
    fun canSaveTag(@PathVariable oj: String, auth: Authentication): ResponseEntity<Void> {
        if (!isUsuarioTemOrgaoJulgador(authService.getUsuario(auth), oj)) {
            return ResponseEntity(FORBIDDEN)
        }
        return ResponseEntity(ACCEPTED)
    }

}
