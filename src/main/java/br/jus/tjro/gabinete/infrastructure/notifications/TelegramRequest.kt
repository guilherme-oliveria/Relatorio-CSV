package br.jus.tjro.gabinete.infrastructure.notifications

import br.jus.tjro.gabinete.infrastructure.extentions.toTime
import br.jus.tjro.gabinete.model.gab.processo.Processo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@Component
class TelegramRequest(val config: TelegramConfig, val template: RestTemplate) {
    init {
        template.messageConverters.add(0, StringHttpMessageConverter(StandardCharsets.UTF_8))
    }
    val logger: Logger = LoggerFactory.getLogger(TelegramRequest::class.java)

    internal fun message(processo: Processo): List<String> {
        return config.notificationMessage(processo, config.template)
    }

    val logTelegramResponse = { response: ResponseEntity<String>, processo : Processo ->
        when(response.statusCodeValue) {
            in 200..299 -> logger.info("Foi informado no telegram que o processo ${processo.numeroProcesso} entrou no Gabinete.")
            else -> logger.warn("""Atenção ocorreu algum erro oo notificar chegada do processo ${processo.numeroProcesso} para o telagram.
                    |\tStatus: ${response.statusCodeValue}
                    |\tBody: ${response.body}
                """.trimMargin())
        }
    }

    internal fun isNotPlantao() : Boolean {
        return !isPlantao(LocalDateTime.now(), this.config.timeRange)
    }

    fun notify(processo: Processo) {

        if(isNotPlantao())
            return

        val headers = HttpHeaders().also {
            it.add("Content-Type", "application/json")
        }
            this.message(processo)
                    .map { body -> HttpEntity(body, headers) }
                    .map { entity ->
                        try {
                            logger.info("Submiting ${entity.body}")
                            template.postForEntity(config.messageUrl, entity, String::class.java)
                        } catch (ex: Throwable) {
                            logger.error("""  Ocorreu um erro ao enviar notificação ao telegram:
                                        |\t${ex.message}
                                        |\t${(ex as RestClientResponseException).responseBodyAsString}
                                        |${ex.stackTrace.joinToString("\n") { "\t\t$it" }}
                                        |""".trimMargin())
                            when(ex) {
                                is RestClientResponseException ->
                                    logger.error("|\t ${ex.responseBodyAsString}".trimMargin())
                            }
                            null
                        }
                    }
                    .forEach { response ->
                        if(response != null )
                            logTelegramResponse(response, processo)
                    }

    }

}

internal fun isPlantao(date: LocalDateTime, range: String? = "07:00-13:00,16:00-18:00"): Boolean {
    val regex = Regex("""(([:\d]+)-([:\d]+))+""")
    val ranges: Sequence<MatchResult> = regex.findAll(range!!)
    val plantaoTurn = ranges
            .map { (it.groupValues[2].toTime()!!..it.groupValues[3].toTime()!!) }
            .any { date.toLocalTime() in it }
    return !(plantaoTurn) || date.dayOfWeek.value > 5
}

@Configuration
class TelegramConfig(@Value("\${telegram.plantao.apiUrl:}") var apiUrl: String,
                     @Value("\${telegram.plantao.token:}") val token: String,
                     @Value("\${telegram.plantao.channelOjs:}") private val chatsOjs: String,
                     @Value("\${telegram.plantao.template:}") val template: String,
                     @Value("\${telegram.plantao.timeRange:@null}") val timeRange: String?) {
    internal fun notificationMessage(processo: Processo, template: String) : List<String> {
        return channelsOjs.channels
                .filter { processo.orgaoJulgadorObj.id in it.ojs || "-42" in it.ojs }
                .map { processo.asMessage(it.channel, processo.asTelegramText(template)) }
    }

    private val channelsOjs: ChannelsOjs = ChannelsOjs(chatsOjs)
    internal val messageUrl: String = "${apiUrl.removeSuffix("/")}/bot$token/sendMessage"
}

val logger: Logger = LoggerFactory.getLogger(TelegramRequest::class.java)

internal fun Processo.asTelegramText(template: String): String {
    if (template.isEmpty())
        logger.error("""Ocorreu um erro ao enviar notificação ao telegram:
            |\t telegram.plantao.template está vazia 
        """.trimMargin())
    return template.replace("\$id", this.id.toString())
            .replace("\$numeroProcesso", this.numeroProcesso)
            .replace("\$orgaoJulgador", this.orgaoJulgadorObj?.descricao ?: "")
            .replace("\$tpuClasse", this.tpuClasse?.descricao ?: "")
}

internal fun Processo.asMessage(channel: Channel, template: String): String {
    return """
        {
            "chat_id": "$channel",
            "text": "${this.asTelegramText(template)}"
        }
    """.trimIndent()
}
