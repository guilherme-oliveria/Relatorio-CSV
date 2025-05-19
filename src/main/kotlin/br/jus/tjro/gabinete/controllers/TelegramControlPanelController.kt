package br.jus.tjro.gabinete.controllers

import br.jus.tjro.gabinete.infrastructure.notifications.TelegramRequest
import br.jus.tjro.gabinete.model.gab.processo.Processo
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/telegram")
class TelegramControlPanelController(@Autowired request: TelegramRequest) {

    private val request: TelegramTestRequest = TelegramTestRequest(request)

    class TelegramTestRequest(request: TelegramRequest) : TelegramRequest(request.config, request.template) {
        override fun isNotPlantao() : Boolean {
            return false
        }
    }

    private val processo = Processo().also {
        it.id = 1
        it.orgaoJulgadorObj = OrgaoJulgador("1-PJEPG").also { it.descricao = "Primeira Vara do Windson" }
        it.numeroProcesso = "4242420-42.2019.8.22.0001"
        it.tpuClasse = TpuClasse(1L).also { it.descricao = "Consulta Administrativa" }
    }

    @GetMapping("/")
    fun greeting() = request.notify(processo)
}
