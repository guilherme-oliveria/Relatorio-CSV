package br.jus.tjro.gabinete.adapters

import br.jus.tjro.gabinete.model.gab.Usuario
import br.jus.tjro.gabinete.model.gab.alvara.Conta
import br.jus.tjro.gabinete.model.gab.alvara.ContaJudicial
import br.jus.tjro.gabinete.model.gab.alvara.InstituicaoFinanceira
import br.jus.tjro.gabinete.model.gab.alvara.PagamentoAlvara
import br.jus.tjro.gabinete.model.gab.enums.TipoPessoaEnum
import br.jus.tjro.gabinete.model.gab.enums.VariavelEnum
import br.jus.tjro.gabinete.model.gab.processo.Processo
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador
import br.jus.tjro.gabinete.service.local.OrgaoJulgadorService
import br.jus.tjro.gabinete.service.local.ProcessoParteService
import br.jus.tjro.gabinete.util.StringUtils
import java.util.*
import java.util.Objects.isNull

open class ModDocProcessoAdapter(
        val processo: Processo,
        val orgaoJulgadorService: OrgaoJulgadorService,
        val usuario: Usuario) : ModeloDocumentoAdapter {

    val mapping = hashMapOf(
            "processo_numero" to listOf(
                    VariavelEnum.PROCESSO_NUMERO),
            "processo_assuntos" to listOf(
                    VariavelEnum.PROCESSO_ASSUNTOS),
            "processo_classe" to listOf(
                    VariavelEnum.PROCESSO_CLASSE),
            "processo_valor" to listOf(
                    VariavelEnum.PROCESSO_VALOR,
                    VariavelEnum.PROCESSO_VALOR_EXTENSO),
            "processo_" to listOf(
                    VariavelEnum.PROCESSO_VALOR,
                    VariavelEnum.PROCESSO_VALOR_EXTENSO),
            "processo_ultima_distribuicao" to listOf(
                    VariavelEnum.PROCESSO_DATA_ULTIMA_DISTRIBUICAO),
            "processo_partes" to listOf(
                    VariavelEnum.POLO_ATIVO_ADVOGADOS,
                    VariavelEnum.POLO_ATIVO_ADVOGADOS_COM_ENDERECO,
                    VariavelEnum.POLO_PASSIVO_ADVOGADOS,
                    VariavelEnum.POLO_PASSIVO_ADVOGADOS_COM_ENDERECO,
                    VariavelEnum.POLO_ATIVO_PARTES,
                    VariavelEnum.POLO_ATIVO_PARTES_COM_CPF,
                    VariavelEnum.POLO_ATIVO_PARTES_COM_CPF_E_ENDERECO,
                    VariavelEnum.POLO_ATIVO_PARTES_COM_ENDERECO,
                    VariavelEnum.POLO_PASSIVO_PARTES,
                    VariavelEnum.POLO_PASSIVO_PARTES_COM_CPF,
                    VariavelEnum.POLO_PASSIVO_PARTES_COM_CPF_E_ENDERECO,
                    VariavelEnum.POLO_PASSIVO_PARTES_COM_ENDERECO),
            "orgao_julgador" to listOf(
                    VariavelEnum.ORGAO_CIDADE,
                    VariavelEnum.ORGAO_UF,
                    VariavelEnum.ORGAO_ENDERECO,
                    VariavelEnum.ORGAO_NOME,
                    VariavelEnum.ORGAO_JUIZ,
                    VariavelEnum.ORGAO_MAGISTRADO
            ),
            "ambiente" to listOf(
                    VariavelEnum.AMBIENTE_PERFIS
            ),
            "alvara" to listOf(
                    VariavelEnum.ALVARA_CONTA_JUDICIAL,
                    VariavelEnum.ALVARA_CONTA_JUD_FAVORECIDO,
                    VariavelEnum.ALVARA_FAVORECIDOS
            ),
            "data" to listOf(
                    VariavelEnum.DATA_HOJE,
                    VariavelEnum.DATA_EXTENSO,
                    VariavelEnum.DATA_EXTENSO_SEM_DIA_SEMANA,
                    VariavelEnum.HORA_AGORA,
                    VariavelEnum.HORA_EXTENSO
            )
    )

    override fun adapt(variaveis: List<String>, assinando: Boolean): HashMap<String, Any> {
        val keys = mapping.filter { it.value.filter { s -> variaveis.contains(s) }.size > 0 }.keys
        val executors = hashMapOf(
                "processo_numero" to { processo.numeroProcesso },
                "processo_assuntos" to { processo.tpuAssuntos?.map { it.descricao } ?: listOf<String>() },
                "processo_classe" to { if (isNull(processo.tpuClasse) || isNull(processo.tpuClasse.descricao)) "" else processo.tpuClasse.descricao },
                "processo_valor" to { if (isNull(processo.valorCausa)) "" else processo.valorCausa },
                "processo_ultima_distribuicao" to { if (isNull(processo.dataUltimaDistribuicao)) "" else processo.dataUltimaDistribuicao },
                "processo_partes" to { partes(processo.processoPartes) },
                "orgao_julgador" to { orgaoJulgador(assinando) },
                "ambiente" to { ambiente() },
                "alvara" to { alvara() },
                "data" to { Date() }
        )

        var mapped = HashMap<String, Any>()

        executors.filterKeys { keys.contains(it) }
            .forEach { key, value ->
                mapped[key] = value()
            }
        return mapped
    }

    private fun alvara(): Any {
        var ret = hashMapOf<String, Any>()
        processo.minutaEmElaboracao().ifPresentOrElse({
            if (!isNull(it.alvara)) {
                val pagamentos = it.alvara.pagamentoAlvara.map {
                    hashMapOf(
                            "conta_judicial" to contaJudicial(it.contaJudicial),
                            "conta" to conta(it.conta),
                            "favorecido" to favorecido(it),
                            "forma_pagamento" to it.formaPagamento,
                            "com_atualizacao" to if(it.comAtualizacao) "true" else "",
                            "valor" to it.valor
                    )
                }

                ret = hashMapOf(
                        "contas_judiciais" to pagamentos.map { it.get("conta_judicial") }.distinctBy { (it as HashMap<*, *>).get("id") },
                        "pagamentos" to pagamentos
                )
            }
        }, { HashMap<String, Any>() })

        return ret
    }

    private fun favorecido(fav: PagamentoAlvara?): Any {
        return hashMapOf(
                "nome" to fav?.nomeFavorecido,
                "documento" to fav?.docFavorecido
        )
    }

    private fun conta(ct: Conta?): Any {
        return hashMapOf(
                "agencia" to ct?.agencia,
                "banco" to InstituicaoFinanceira.descricaoOfCodigo(ct?.banco),
                "conta" to ct?.contaCorrenteComDigito,
                "operacao" to ct?.operacao
        )
    }

    private fun contaJudicial(cj: ContaJudicial?): Any {
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


    private fun ambiente(): Any {
        val arrPermissoes: List<String> = usuario.permissoes
        val perfis = if (arrPermissoes != null) java.lang.String.join(", ", arrPermissoes) else ""

        return hashMapOf(
                "login" to usuario.cpf,
                "perfis" to perfis
        )
    }

    private fun orgaoJulgador(assinando: Boolean): Any {
        val orgaoJulgador = processo.orgaoJulgadorObj
        val enderecoOrg = if(isNull(orgaoJulgador)) null else orgaoJulgador.enderecos.lastOrNull()
        val nome = if (isNull(orgaoJulgador.descricao)) "" else orgaoJulgador.descricao
        var endereco = ""
        var cidade = ""
        var uf = ""
        val juiz = usuario.nome

        if (!isNull(enderecoOrg)) {
            with(enderecoOrg, {
                endereco = this?.enderecoCompleto ?: ""
                cidade = this?.municipio ?: ""
                uf = this?.uf ?: ""
            })
        }

        return hashMapOf(
                "nome" to nome,
                "endereco" to endereco,
                "uf" to uf,
                "cidade" to cidade,
                "juiz" to if (assinando) juiz else "{" + VariavelEnum.ORGAO_MAGISTRADO + "}"
        )
    }

    private fun partes(parteProcesso: List<ProcessoParte>): Any {
        val partes = parteProcesso
                .map {
                    hashMapOf(
                            "nome" to (if (it.sobSegredo) StringUtils.obtemIniciais(it.nome) else it.nome),
                            "tipo_parte" to it.tipoParte,
                            "tipo_pessoa" to it.pessoa.tipoPessoa.toString(),
                            "endereco" to endereco(it),
                            "polo" to it.tipoPolo.label,
                            "procuradoria" to if (isNull(it.procuradoria)) "" else it.procuradoria,
                            "documento" to documento(it)
                    )
                }
        val lista = ArrayList<HashMap<String, String>>(partes)
        partes.filter { !it.get("procuradoria").isNullOrEmpty() }
                .forEach {
                    lista.add(hashMapOf<String, String>(
                            "nome" to it.getOrDefault("procuradoria", ""),
                            "documento" to "",
                            "endereco" to "",
                            "tipo_parte" to "advogado",
                            "polo" to it.getOrDefault("polo", ""),
                            "tipo_pessoa" to "",
                            "procuradoria" to ""
                    ))
                }
        return lista
    }

    private fun endereco(processoParte: ProcessoParte): String {
        if (processoParte.processoParteEnderecos.isEmpty())
            return ""
        processoParte.processoParteEnderecos.sortBy { it.id }
        val processoParteEndereco = processoParte.processoParteEnderecos.last()
        return if (isNull(processoParteEndereco)) "" else processoParteEndereco.endereco.enderecoCompleto
    }

    private fun documento(processoParte: ProcessoParte): String {
        var documento = ""
        var individuo = processoParte.pessoa
        if (individuo == null) return ""
        if (processoParte.isProcuradoria) {
            documento = ""
        } else if (processoParte.tipoParte.equals("ADVOGADO")) {
            documento = individuo.getDocumentoPorTipo("OAB")
        } else if (individuo.tipoPessoa.equals(TipoPessoaEnum.F)) {
            documento = individuo.getDocumentoPorTipo("CPF")
        } else if (individuo.tipoPessoa.equals(TipoPessoaEnum.J)) {
            documento = individuo.getDocumentoPorTipo("CPJ")
        }
        return documento
    }

}
