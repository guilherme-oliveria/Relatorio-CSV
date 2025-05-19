package br.jus.tjro.gabinete.infrastructure.filters

import br.jus.tjro.gabinete.model.gab.transiente.VariavelTemplate.VariavelTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import kotlin.text.Regex as r

@Component
class VariaveisFilter(@Value("\${docs.exclusions:''}") excludeString: String) {
    internal val grupoExclusions: List<String>
    internal val chaveExclusions: List<String>

    private val isGrupo = { p: String -> p.matches(r("""\([\w\d]+\)""")) }

    init {
        val exclusions = if (excludeString.isEmpty()) emptyList() else excludeString.toLowerCase().split(',')
        this.chaveExclusions = exclusions.filter{!isGrupo(it)}.map { it.trim() }
        this.grupoExclusions = exclusions.filter(isGrupo).map { it.replace(r("[()]"), "") }
    }

    internal fun isChaveExclusion(chave: String): Boolean = this.chaveExclusions.any { chave.toLowerCase().matches(r(it)) }
    internal fun isGrupoExclusion(grupo: String): Boolean = this.grupoExclusions.any { grupo.toLowerCase().matches(r(it)) }

    fun chaveIsExclude(v: VariavelTemplate) = this.isChaveExclusion(v.chave)
    fun grupoIsExclude(v: VariavelTemplate) = this.isGrupoExclusion(v.grupo)
}
