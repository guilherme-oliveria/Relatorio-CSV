package br.jus.tjro.gabinete

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import br.jus.tjro.gabinete.adapters.ModDocProcessoAdapter
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum
import br.jus.tjro.gabinete.model.gab.Usuario
import br.jus.tjro.gabinete.model.gab.alvara.*
import br.jus.tjro.gabinete.model.gab.enums.TipoPessoaEnum
import br.jus.tjro.gabinete.model.gab.enums.TipoPolo
import br.jus.tjro.gabinete.model.gab.enums.VariavelEnum
import br.jus.tjro.gabinete.model.gab.minuta.Minuta
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa
import br.jus.tjro.gabinete.model.gab.processo.Processo
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte
import br.jus.tjro.gabinete.service.local.OrgaoJulgadorService
import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.io.Serializable
import java.util.*
import org.mockito.Mockito.`when` as mockWhen

class ModDocProcessoAdapterTests {
    @Test
    fun `Deve instanciar o adapter de processo corretamente`() {
        val processo = Processo()
        val orgaoJulgadorService = mock(OrgaoJulgadorService::class.java)
        val usuario = Usuario("windson", "666", listOf(""), "token")
        val modDocProcessoAdapter = ModDocProcessoAdapter(processo, orgaoJulgadorService, usuario)
        assertNotNull(modDocProcessoAdapter)
    }

    @Test
    fun `Deve adaptar somente o numero de processo`() {
        val processo = mock(Processo::class.java)
        mockWhen(processo.numeroProcesso).thenReturn("123")
        val orgaoJulgadorService = mock(OrgaoJulgadorService::class.java)
        val usuario = Usuario("windson", "666", listOf(""), "token")
        val modDocProcessoAdapter = ModDocProcessoAdapter(processo, orgaoJulgadorService, usuario)
        val variaveis = listOf(VariavelEnum.PROCESSO_NUMERO)
        assertEquals("123", processo.numeroProcesso)
        assertEquals(hashMapOf("processo_numero" to "123"), modDocProcessoAdapter.adapt(variaveis,false))
        verify(processo, atLeast(0)).tpuAssuntos
        verify(processo, atLeast(1)).numeroProcesso
    }

    @Test
    fun `Deve adaptar as partes`() {
        val orgaoJulgadorService = mock(OrgaoJulgadorService::class.java)
        val usuario = Usuario("windson", "666", listOf(""), "token")
        val processo = Processo()
        val parte = arrayListOf(ProcessoParte(1L, "requerente", null, TipoPolo.A, processo,
                Pessoa(1L, "windson", null, Date(), null, TipoPessoaEnum.F,FonteDadosEnum.PJEPG)))
        processo.processoPartes = parte

        val modDocProcessoAdapter = ModDocProcessoAdapter(processo, orgaoJulgadorService, usuario)
        val parteHash = hashMapOf(
                "nome" to "windson",
                "tipo_parte" to "requerente",
                "tipo_pessoa" to "F",
                "endereco" to "",
                "polo" to "Ativo",
                "procuradoria" to "",
                "documento" to "DESCONHECIDO"
        )

        assertEquals(hashMapOf("processo_partes" to listOf(parteHash)), modDocProcessoAdapter.adapt(listOf(VariavelEnum.POLO_ATIVO_PARTES),false))
    }

    @Test
    fun `Deve adaptar as partes sob segredo de justica`() {
        val orgaoJulgadorService = mock(OrgaoJulgadorService::class.java)
        val usuario = Usuario("windson", "666", listOf(""), "token")
        val processo = Processo()
        processo.isSegredoJustica = true
        val parte1 = ProcessoParte(1L, "requerente", null, TipoPolo.A, processo,
                Pessoa(1L, "Windson Gedson", null, Date(), null, TipoPessoaEnum.F,FonteDadosEnum.PJEPG))
        processo.processoPartes = mutableListOf(parte1)
        val modDocProcessoAdapter = ModDocProcessoAdapter(processo, orgaoJulgadorService, usuario)

        val parteHash = hashMapOf(
                "nome" to "W. G.",
                "tipo_parte" to "requerente",
                "tipo_pessoa" to "F",
                "endereco" to "",
                "polo" to "Ativo",
                "procuradoria" to "",
                "documento" to "DESCONHECIDO"
        )

        assertEquals(hashMapOf("processo_partes" to listOf(parteHash)), modDocProcessoAdapter.adapt(listOf(VariavelEnum.POLO_ATIVO_PARTES),false))
    }

    @Test
    fun `Deve adaptar as contas judiciais do alvara`() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures")
        val minuta = Fixture.from(Minuta::class.java).gimme<Minuta>("validComAlvara")
        val processo = mock(Processo::class.java)
        Mockito.`when`(processo. minutaEmElaboracao()).thenReturn(Optional.of(minuta))
        minuta.processo = processo
        val orgaoJulgadorService = mock(OrgaoJulgadorService::class.java)
        val usuario = Usuario("windson", "666", listOf(""), "token")
        val modDocProcessoAdapter = ModDocProcessoAdapter(processo, orgaoJulgadorService, usuario)

        val alvara = minuta.alvara
        val contasJudiciais = alvara.pagamentoAlvara
            .map { it -> it.contaJudicial }
            .distinctBy { it -> it.id }
            .map {
                hashMapOf(
                    "id" to it.codLegado,
                    "agencia" to it.agencia,
                    "conta" to it.contaCorrenteComDigito,
                    "banco" to InstituicaoFinanceira.descricaoOfCodigo(it.codBanco),
                    "valor_guia" to it.valorGuia,
                    "saldo" to it.saldo?.valor,
                    "saldo_atualizado" to it.valorAtualizado
                )
            }
        val rendered = modDocProcessoAdapter.adapt(listOf(VariavelEnum.ALVARA_CONTA_JUDICIAL),false)
        val actualContasJudiciais = (rendered.get("alvara") as HashMap<*, *>).get("contas_judiciais")
        assertEquals(contasJudiciais, actualContasJudiciais)
    }

    @Test
    fun `Deve adaptar o alvara`() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures")
        val minuta = Fixture.from(Minuta::class.java).gimme<Minuta>("validComAlvara")
        val processo = mock(Processo::class.java)
        Mockito.`when`(processo. minutaEmElaboracao()).thenReturn(Optional.of(minuta))
        minuta.processo = processo
        val orgaoJulgadorService = mock(OrgaoJulgadorService::class.java)
        val usuario = Usuario("windson", "666", listOf(""), "token")
        val modDocProcessoAdapter = ModDocProcessoAdapter(processo, orgaoJulgadorService, usuario)
        val rendered = modDocProcessoAdapter.adapt(listOf(VariavelEnum.ALVARA_FAVORECIDOS),false)

        val getContaJud = fun (cj: ContaJudicial?): HashMap<String, Any?> {
            return hashMapOf(
                "id" to cj?.codLegado,
                "agencia" to cj?.agencia,
                "conta" to cj?.contaCorrenteComDigito,
                "banco" to InstituicaoFinanceira.descricaoOfCodigo(cj?.codBanco),
                "valor_guia" to cj?.valorGuia,
                "saldo" to cj?.saldo?.valor,
                "saldo_atualizado" to cj?.valorAtualizado
            )
        }

        val getConta = fun (ct: Conta?): HashMap<String, Any?> {
            return hashMapOf(
                "agencia" to ct?.agencia,
                "banco" to InstituicaoFinanceira.descricaoOfCodigo(ct?.banco),
                "conta" to ct?.contaCorrenteComDigito,
                "operacao" to ct?.operacao
            )
        }

        val getFavorecido = fun(fav: PagamentoAlvara?): HashMap<String, Any?> {
            return hashMapOf(
                "nome" to fav?.nomeFavorecido,
                "documento" to fav?.docFavorecido
            )
        }

        val getPagamentos = fun(alvara: Alvara): List<HashMap<String, Serializable>> {
            return alvara.pagamentoAlvara.map {
                hashMapOf(
                    "conta_judicial" to getContaJud(it.contaJudicial),
                    "conta" to getConta(it.conta),
                    "favorecido" to getFavorecido(it),
                    "forma_pagamento" to it.formaPagamento,
                    "com_atualizacao" to if(it.comAtualizacao) "true" else "",
                    "valor" to it.valor
                )
            }.toList()
        }

        val alvara = minuta.alvara
        val expected = hashMapOf("alvara" to hashMapOf(
            "pagamentos" to getPagamentos(alvara),
            "contas_judiciais" to getPagamentos(alvara).map { it.get("conta_judicial") }
        ))
        assertEquals(expected, rendered)
    }

}
