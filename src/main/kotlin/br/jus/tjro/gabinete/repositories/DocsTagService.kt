package br.jus.tjro.gabinete.repositories

import br.jus.tjro.gabinete.model.docs.ModeloDocumentoEnv
import br.jus.tjro.gabinete.model.docs.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class DocsTagService {

    @Autowired
    lateinit var rest: RestTemplate

    @Autowired
    lateinit var env: ModeloDocumentoEnv

    val headers: HttpHeaders = HttpHeaders().apply {
        this.contentType = APPLICATION_JSON
    }

    fun save(tag: Tag): ResponseEntity<Tag> =
        rest.exchange("""${this.env.backUrl}/tags""", PUT, HttpEntity(tag, headers), Tag::class.java)

    fun findByDescricao(sistema: String, orgaoJulgador: String?, term: String?): ResponseEntity<Array<Tag>> =
        rest.getForEntity("""${this.env.backUrl}/tags/$sistema?orgaoJulgador=$orgaoJulgador&term=$term""", Array<Tag>::class.java)

    @PreAuthorize("canEditOrDeleteModeloDocumento(#modelo)")
    fun delete(modelo: String, tag: String): ResponseEntity<Void> =
        rest.exchange("""${this.env.backUrl}/tags/$modelo/$tag""", DELETE, null, Void::class.java)
}
