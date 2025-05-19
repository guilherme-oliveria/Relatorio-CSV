package br.jus.tjro.gabinete.model.gab.alvara;

import br.jus.tjro.gabinete.model.gab.processo.Processo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GerarAlvara27Request extends GerarAlvaraRequest{
    private Long contaOrigemId;
    private String dataValidade;
    private String dataAtualizacao;
    private Long orgaoId;
    private Integer codComarca;
    private String numeroProcesso;
    private Long idDeposito;
    private String codBanco;
    private String agenciaDestino;
    private BigDecimal valorATransferir;
    private String nomeDepositante;
    private TipoPessoa tipoPessoaDepositante;
    private String observacoes;

    private String idOrigem;

    public GerarAlvara27Request(PagamentoAlvara pagamentoAlvara, Long orgaoJulgadorDepara, Processo processo, Requisitante requisitante, LocalDateTime dataCriacao) {
        contaOrigemId = Long.valueOf(pagamentoAlvara.getContaJudicial().getCodLegado());
        orgaoId = orgaoJulgadorDepara;
        numeroProcesso = processo.numeroProcessoSemFormatacao();
        valorATransferir = pagamentoAlvara.getValor();
        nomeDepositante = pagamentoAlvara.getNomeFavorecido();
        tipoPessoaDepositante = TipoPessoa.PESSOA_JURIDICA;
        setRequisitante(requisitante);
        if (pagamentoAlvara.getComAtualizacao()) {
            this.dataAtualizacao = DateTimeFormatter.ofPattern("yyyyMMdd").format(dataCriacao);
        }
        idOrigem = pagamentoAlvara.getId().toString();
    }

    public Long getContaOrigemId() {
        return contaOrigemId;
    }

    public void setContaOrigemId(Long contaOrigemId) {
        this.contaOrigemId = contaOrigemId;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(String dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getOrgaoId() {
        return orgaoId;
    }

    public void setOrgaoId(Long orgaoId) {
        this.orgaoId = orgaoId;
    }

    public Integer getCodComarca() {
        return codComarca;
    }

    public void setCodComarca(Integer codComarca) {
        this.codComarca = codComarca;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public Long getIdDeposito() {
        return idDeposito;
    }

    public void setIdDeposito(Long idDeposito) {
        this.idDeposito = idDeposito;
    }

    public String getCodBanco() {
        return codBanco;
    }

    public void setCodBanco(String codBanco) {
        this.codBanco = codBanco;
    }

    public String getAgenciaDestino() {
        return agenciaDestino;
    }

    public void setAgenciaDestino(String agenciaDestino) {
        this.agenciaDestino = agenciaDestino;
    }

    public BigDecimal getValorATransferir() {
        return valorATransferir;
    }

    public void setValorATransferir(BigDecimal valorATransferir) {
        this.valorATransferir = valorATransferir;
    }

    public String getNomeDepositante() {
        return nomeDepositante;
    }

    public void setNomeDepositante(String nomeDepositante) {
        this.nomeDepositante = nomeDepositante;
    }

    public TipoPessoa getTipoPessoaDepositante() {
        return tipoPessoaDepositante;
    }

    public void setTipoPessoaDepositante(TipoPessoa tipoPessoaDepositante) {
        this.tipoPessoaDepositante = tipoPessoaDepositante;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getIdOrigem() {
        return idOrigem;
    }

    public void setIdOrigem(String idOrigem) {
        this.idOrigem = idOrigem;
    }
}
