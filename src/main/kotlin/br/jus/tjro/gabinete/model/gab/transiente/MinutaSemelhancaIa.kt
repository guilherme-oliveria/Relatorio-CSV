package br.jus.tjro.gabinete.model.gab.transiente



data class MinutaSemelhancaIa(
        val id: Long,
        val similaridade: Map<String, Double>
)

data class ContainerMinutaSemelhancaIA(
        val minutas: List<MinutaSemelhancaIa>
) {
    fun filtrarMinutasPorSemelhancaMinima(semelhancaMinima: Double, idProcessoReferencia: Long) = ContainerMinutaSemelhancaIA(
            minutas.filter { minuta ->
                minuta.id == idProcessoReferencia || minuta.similaridade.any { sim ->
                    sim.key.equals(idProcessoReferencia.toString()) && sim.value >= semelhancaMinima
                }
            }
    )
}
