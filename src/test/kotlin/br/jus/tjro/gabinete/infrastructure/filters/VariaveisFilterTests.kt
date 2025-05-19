package br.jus.tjro.gabinete.infrastructure.filters

import br.jus.tjro.gabinete.model.gab.enums.VariavelEnum
import br.jus.tjro.gabinete.model.gab.transiente.VariavelTemplate.VariavelTemplate
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

class VariaveisFilterTests {

    @Test
    fun `deve filtrar`() {
        val filter = VariaveisFilter(".*juiz.*,.*magistrado.*,(alvara)")
        val essaFica = VariavelTemplate()
        essaFica.chave = VariavelEnum.PROCESSO_NUMERO
        essaFica.grupo = "processo"

        val essaSai = VariavelTemplate()
        essaSai.chave = VariavelEnum.ORGAO_JUIZ
        essaSai.grupo = "orgaojulgador"

        val essaSaiTb = VariavelTemplate()
        essaSaiTb.chave = VariavelEnum.ALVARA_CONTA_JUDICIAL
        essaSaiTb.grupo = "alvara"

        listOf(essaFica, essaSai, essaSaiTb)
            .filter { !filter.chaveIsExclude(it) }
            .filter { !filter.grupoIsExclude(it) }
            .size `should be equal to` 1
    }
}
