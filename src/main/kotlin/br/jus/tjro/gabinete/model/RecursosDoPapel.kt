package br.jus.tjro.gabinete.model

import br.jus.tjro.gabinete.model.gab.segredo.recurso.Recursos
import br.jus.tjro.gabinete.model.gab.segredo.recurso.*


/**
 * Doc
 * papeis: lista de perfils do sistema relacionado a strings de perfils do pje!
 * Exemplo: magistrado no sistema esta relacionado a "juiz","desembargador" já que esses valores vem do PJEPG e PJESG.
 */

class RecursosDoPapel(papeis: Map<String, List<String>>) {

    init {
        if (papeis["administrador"] != null)
            administrador = papeis["administrador"]!!
        if (papeis["assessor"] != null)
            assessor = papeis["assessor"]!!
        if (papeis["magistrado"] != null)
            magistrado = papeis["magistrado"]!!
    }

    companion object {
        private var assessor: List<String> = listOf("assessor")
        private var administrador: List<String> = listOf("administrador")
        private var magistrado: List<String> = listOf("magistrado")

        @JvmStatic
        fun get(papel: String): Recursos {
            if (administrador.any { it.lowercase() == papel.lowercase() })
                return AdministradorRecurso()
            if (magistrado.any { it.lowercase() == papel.lowercase() })
                return MagistradoRecurso()
            if (assessor.any { it.lowercase() == papel.lowercase() })
                return AssessorRecurso()
            return SemRecurso()
        }
    }
}
