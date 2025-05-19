package br.jus.tjro.gabinete.model.gab.transiente

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


open class MinutaSemelhancaIaTests {

    @Test
    fun deveFiltrarAsMinutasComScoreMinimoDeSemelhanca() {

        val minutaRecebidaAPI = ContainerMinutaSemelhancaIA(listOf(
                MinutaSemelhancaIa(
                        1,
                        hashMapOf("1" to 1.0, "2" to 0.3, "3" to 0.7)
                ),
                MinutaSemelhancaIa(
                        2,
                        hashMapOf("1" to 0.3, "2" to 1.0, "3" to 0.2)
                ),
                MinutaSemelhancaIa(
                        3,
                        hashMapOf("1" to 0.7, "2" to 0.2, "3" to 1.0)
                )
        ))

        assertEquals(minutaRecebidaAPI
                .filtrarMinutasPorSemelhancaMinima(0.5, 1),
                ContainerMinutaSemelhancaIA(listOf(
                        MinutaSemelhancaIa(
                                1,
                                hashMapOf("1" to 1.0, "2" to 0.3, "3" to 0.7)
                        ),
                        MinutaSemelhancaIa(
                                3,
                                hashMapOf("1" to 0.7, "2" to 0.2, "3" to 1.0)
                        )
                ))

        )

        assertEquals(minutaRecebidaAPI
                .filtrarMinutasPorSemelhancaMinima(0.71, 3),
                ContainerMinutaSemelhancaIA(listOf(
                        MinutaSemelhancaIa(
                                3,
                                hashMapOf("1" to 0.7, "2" to 0.2, "3" to 1.0)
                        )
                ))
        )


    }

}
