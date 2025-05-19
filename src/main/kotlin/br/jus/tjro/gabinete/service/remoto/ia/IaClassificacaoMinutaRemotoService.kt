package br.jus.tjro.gabinete.service.remoto.ia

import br.jus.tjro.gabinete.model.gab.minuta.Minuta
import br.jus.tjro.gabinete.model.gab.transiente.ContainerMinutaSemelhancaIA
import br.jus.tjro.gabinete.service.remoto.RemotoServiceAbstract
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import java.util.*
import org.springframework.web.client.RestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*

inline fun <reified T : Any> typeRef(): ParameterizedTypeReference<T> = object : ParameterizedTypeReference<T>() {}

const val percentualMinimoSemelhancaMinutas = 0.5

@Service
class IaClassificacaoMinutaRemotoService(
        @Value("\${URL_SINAPSES_REST:http://ia-dev.cnj.jus.br/sinapses-backend/rest/modelos/executarServico/-tjro-jud/}")
        val URLSinapses: String,
        @Value("\${AUTHORIZATION_SINAPSES_REST:Basic Z2FiaW5ldGU6Tm1FeU56QTRaVGt5TlRVek9Ua3lZV001WWpsaFpXVTA}")
        val AuthorizationSinapsesREST: String,
        @Autowired
        val rt: RestTemplate
): RemotoServiceAbstract() {

    override fun getRestTemplate() = rt

    @ExperimentalStdlibApi
    fun pesquisarMinutasSimilares(minutas: List<Minuta>, idProcessoReferencia: Long): ContainerMinutaSemelhancaIA? {
        val headers = HttpHeaders()
        val payload = mapOf("minutas" to minutas.map { minuta ->
            mapOf("id" to minuta.processo.id,
                    "conteudo" to Base64.getEncoder().encodeToString(minuta.minutaHtml.encodeToByteArray()))
        })

        headers.set("Authorization", AuthorizationSinapsesREST)
        headers.set("Content-Type", "application/json")

        val resultado = exchange("${URLSinapses}gen_similaridade_minuta/1",
                HttpMethod.POST,
                HttpEntity(payload, headers),
                ContainerMinutaSemelhancaIA::class.java, "Não foi possível contatar o sistema Sinapses para a verificação de minutas semelhantes. Caso o problema persista, contate o administrador do sistema")

        resultado?.body?.let {
            return it.filtrarMinutasPorSemelhancaMinima(percentualMinimoSemelhancaMinutas, idProcessoReferencia)
        }
        return null
    }
}
