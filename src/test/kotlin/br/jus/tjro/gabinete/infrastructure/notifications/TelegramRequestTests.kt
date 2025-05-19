package br.jus.tjro.gabinete.infrastructure.notifications

import br.jus.tjro.gabinete.infrastructure.extentions.toTime
import br.jus.tjro.gabinete.model.gab.processo.Processo
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be`
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime.of
import java.time.LocalTime.of as of

class TelegramRequestTests {

    private val processo = Processo().also {
        it.id = 1
        it.orgaoJulgadorObj = OrgaoJulgador("PJEPG-1").also { it.descricao = "Primeira Vara do Windson" }
        it.numeroProcesso = "4242420-42.2019.8.22.0001"
        it.tpuClasse = TpuClasse(1L).also { it.descricao = "Consulta Administrativa" }
    }

    private val template = "O processo \$numeroProcesso acabou de entrar no Gabinete https://gabinete.tjro.jus.br/processo/\$id"

    @Test
    fun `processo deve ser capaz de virar uma mensagem do Telegram`() {
        val config = TelegramConfig("https://api.telegram.org/", "617705903:AAGrkSCNzdUcIEqxNZRO9JjxNtKvtbmV5bk", "-1001069957570 -> PJEPG-1 PJEPG-2, -1001120293608 -> PJEPG-2, 247440816 -> PJEPG-1", template, null)
        config.messageUrl `should be equal to` "https://api.telegram.org/bot617705903:AAGrkSCNzdUcIEqxNZRO9JjxNtKvtbmV5bk/sendMessage"
        val telegram = TelegramRequest(config, RestTemplate())
        telegram.message(processo)
            .joinToString("\n")
            .trimIndent()`should be equal to` """
                    {
                        "chat_id": "-1001069957570",
                        "text": "O processo 4242420-42.2019.8.22.0001 acabou de entrar no Gabinete https://gabinete.tjro.jus.br/processo/1"
                    }
                    {
                        "chat_id": "247440816",
                        "text": "O processo 4242420-42.2019.8.22.0001 acabou de entrar no Gabinete https://gabinete.tjro.jus.br/processo/1"
                    }
                """.trimIndent()
    }

    @Test
    fun `o canal do Telegram deve ser capaz de pegar todos os processo`() {
        val config = TelegramConfig("https://api.telegram.org/", "617705903:AAGrkSCNzdUcIEqxNZRO9JjxNtKvtbmV5bk", "-1001069957570 -> PJEPG-1 PJEPG-2, -1001120293608 -> all, 247440816 -> PJEPG-1", template, null)
        config.messageUrl `should be equal to` "https://api.telegram.org/bot617705903:AAGrkSCNzdUcIEqxNZRO9JjxNtKvtbmV5bk/sendMessage"
        val telegram = TelegramRequest(config, RestTemplate())
        telegram.message(processo).joinToString("\n").trimIndent() `should be equal to` """
                                                                                            {
                                                                                                "chat_id": "-1001069957570",
                                                                                                "text": "O processo 4242420-42.2019.8.22.0001 acabou de entrar no Gabinete https://gabinete.tjro.jus.br/processo/1"
                                                                                            }
                                                                                            {
                                                                                                "chat_id": "-1001120293608",
                                                                                                "text": "O processo 4242420-42.2019.8.22.0001 acabou de entrar no Gabinete https://gabinete.tjro.jus.br/processo/1"
                                                                                            }
                                                                                            {
                                                                                                "chat_id": "247440816",
                                                                                                "text": "O processo 4242420-42.2019.8.22.0001 acabou de entrar no Gabinete https://gabinete.tjro.jus.br/processo/1"
                                                                                            }
                                                                                        """.trimIndent()
    }

    @Test
    fun `nenhuma exception deve ocorrer quando o mapeamento dos canais estiver em branco`() {
        val config = TelegramConfig("https://api.telegram.org/", "617705903:AAGrkSCNzdUcIEqxNZRO9JjxNtKvtbmV5bk", "", template, null)
        val telegram = TelegramRequest(config, RestTemplate())
        telegram.message(processo) shouldHaveSize 0
    }

    @Test
    fun `converte chat com list de orgaoJulgador`() {
        val chatsOjs = "-1001069957570 -> 1 2, -1001120293608 -> 2, 247440816 -> 1"
        val channelsOjs = ChannelsOjs(chatsOjs)

        channelsOjs.channels[0].channel `should be equal to` "-1001069957570"
        channelsOjs.channels[1].channel `should be equal to` "-1001120293608"
        channelsOjs.channels[2].channel `should be equal to` "247440816"

        channelsOjs.channels[0].ojs shouldContainAll listOf("1", "2")
        channelsOjs.channels[1].ojs shouldContainAll listOf("2")
        channelsOjs.channels[2].ojs shouldContainAll listOf("1")

        val chatsOjs2 = "-1001120293608 -> all, -380083537 -> PJEPG-98 PJEPG-87 PJEPG-111 PJEPG-88 PJEPG-112 PJEPG-89 PJEPG-90 PJEPG-91 PJEPG-92 PJEPG-95 PJEPG-96 PJEPG-97, -321406513 -> PJEPG-174 PJEPG-159 PJEPG-161 PJEPG-175 PJEPG-160 PJEPG-176 PJEPG-177, -357325508 -> PJEPG-109 PJEPG-99 PJEPG-4 PJEPG-2 PJEPG-110 PJEPG-101 PJEPG-5 PJEPG-102 PJEPG-3 PJEPG-105 PJEPG-7 PJEPG-114 PJEPG-113"
        val channelsOjs2 = ChannelsOjs(chatsOjs2)

        channelsOjs2.channels[0].ojs shouldContainAll listOf("-42")
        channelsOjs2.channels[1].ojs shouldContainAll listOf("PJEPG-98", "PJEPG-87", "PJEPG-111", "PJEPG-88", "PJEPG-112", "PJEPG-89", "PJEPG-90", "PJEPG-91", "PJEPG-92", "PJEPG-95", "PJEPG-96", "PJEPG-97")
        channelsOjs2.channels[2].ojs shouldContainAll listOf("PJEPG-174", "PJEPG-159", "PJEPG-161", "PJEPG-175", "PJEPG-160", "PJEPG-176", "PJEPG-177")
        channelsOjs2.channels[3].ojs shouldContainAll listOf("PJEPG-109", "PJEPG-99", "PJEPG-4", "PJEPG-2", "PJEPG-110", "PJEPG-101", "PJEPG-5", "PJEPG-102", "PJEPG-3", "PJEPG-105", "PJEPG-7", "PJEPG-114", "PJEPG-113")
    }

    @Test
    fun `deve formatar corretamente a messagem para o request`() {
        val json = """
            {
                "chat_id": "-1001069957570",
                "text": "O processo 4242420-42.2019.8.22.0001 acabou de entrar no Gabinete https://gabinete.tjro.jus.br/processo/1"
            }
        """.trimIndent()

        processo.asMessage("-1001069957570", template) `should be equal to` json
    }

    @Test
    fun `testa se é plantão`() {
        isPlantao(of(2019, 12, 2, 18, 1)) `should be` true
        isPlantao(of(2019, 12, 2, 18, 0)) `should be` false
        isPlantao(of(2019, 12, 2, 13, 1)) `should be` true
        isPlantao(of(2019, 12, 2, 13, 0)) `should be` false
        isPlantao(of(2019, 12, 2, 7, 0))  `should be` false
        isPlantao(of(2019, 12, 2, 6, 59)) `should be` true

        isPlantao(of(2019, 12, 7, 17, 0)) `should be` true
        isPlantao(of(2019, 12, 6, 17, 0)) `should be` false
    }

    @Test
    fun `testa parser dos periodos de plantão` () {
        val range = "8-12:01,16:02-19"
        isPlantao(of(2020, 3, 23, 7, 0), range)  `should be` true
        isPlantao(of(2020, 3, 23, 8, 12), range) `should be` false
    }

    @Test
    fun `testa parser de string to date` () {
        "12:0".toTime() `should be` of(12, 0)
        "12".toTime() `should be` of(12, 0)
        "1:08".toTime().toString() `should be equal to` of(1, 8).toString()
    }
}
