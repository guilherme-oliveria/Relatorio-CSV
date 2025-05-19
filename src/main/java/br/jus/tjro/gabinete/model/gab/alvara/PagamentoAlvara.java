package br.jus.tjro.gabinete.model.gab.alvara;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Entity
@Table(name = "pagamento_alvara")
@SequenceGenerator(name = PagamentoAlvara.SEQUENCE_NAME, sequenceName = PagamentoAlvara.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
@Where(clause = PagamentoAlvara.NomeColunaAtivo + "= true")
public class PagamentoAlvara {
    public static final String SEQUENCE_NAME = "SEQ_PAGAMENTO_ALVARA";
    public static final String NomeColunaAtivo = "ativo";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;
    @Column(name = "nome_favorecido")
    @NotNull
    private String nomeFavorecido;
    @Column(name = "doc_favorecido")
    @NotNull
    private String docFavorecido;
    @ManyToOne
    @JoinColumn(name = "id_alvara")
    @JsonIgnore
    private Alvara alvara;
    @Column(name = "nome_sacador")
    private String nomeSacador;
    @Column(name = "documento_sacador")
    private String documentoSacador;
    @Column(name = "nome_sacador_2")
    private String nomeSacador2;
    @Column(name = "documento_sacador_2")
    private String documentoSacador2;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_CONTA")
    private Conta conta;
    @Column(name = "forma_pagamento")
    @NotNull
    private String formaPagamento;
    @Column(name = "com_atualizacao")
    @NotNull
    private Boolean comAtualizacao;
    @NotNull
    private BigDecimal valor;
    @Column(name = "validade")
    private Integer validade;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_CONTA_JUDICIAL")
    private ContaJudicial contaJudicial;
    private boolean enviado = false;
    private boolean ativo = true;
    @Transient
    private String idFavorecido;
    public PagamentoAlvara() {
    }

    public PagamentoAlvara(@JsonProperty("nomeFavorecido") String nomeFavorecido, @JsonProperty("docFavorecido") String docFavorecido,
                           @JsonProperty("conta") Conta conta, @JsonProperty("formaPagamento") String formaPagamento,
                           @JsonProperty("comAtualizacao") Boolean comAtualizacao, @JsonProperty("valor") BigDecimal valor,
                           @JsonProperty("contaJudicial") ContaJudicial contaJudicial, @JsonProperty("enviado") boolean enviado,
                           @JsonProperty("idFavorecido") String idFavorecido, @JsonProperty("nomeSacador") String nomeSacador,
                           @JsonProperty("documentoSacador") String documentoSacador, @JsonProperty("nomeSacador2") String nomeSacador2,
                           @JsonProperty("documentoSacador2") String documentoSacador2, @JsonProperty("validade") Integer validade) {
        this.nomeFavorecido = nomeFavorecido;
        this.docFavorecido = docFavorecido;
        this.conta = conta;
        this.formaPagamento = formaPagamento;
        this.comAtualizacao = comAtualizacao;
        this.valor = valor;
        this.contaJudicial = contaJudicial;
        this.enviado = enviado;
        this.idFavorecido = idFavorecido;
        this.nomeSacador = nomeSacador;
        this.documentoSacador = documentoSacador;
        this.nomeSacador2 = nomeSacador2;
        this.documentoSacador2 = documentoSacador2;
        this.validade = validade;
    }
    public String getValorFormatado() {
        if (this.getValor() == null) return "0,00";
        return formataValor(this.getValor());
    }
    public boolean temNomeFavorecido() {
        return !this.nomeFavorecido.equals("");
    }
    private String formataValor(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor);
    }
    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }
    public void deletar() {
        this.ativo = false;
    }
    public void enviado() {
        this.enviado = true;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNomeFavorecido() {
        return nomeFavorecido;
    }
    public String getDocFavorecido() {
        return docFavorecido;
    }
    public Conta getConta() {
        return conta;
    }
    public String getFormaPagamento() {
        return formaPagamento;
    }
    public Boolean getComAtualizacao() {
        return comAtualizacao;
    }
    public BigDecimal getValor() {
        return valor;
    }
    public ContaJudicial getContaJudicial() {
        return contaJudicial;
    }
    public boolean isEnviado() {
        return enviado;
    }
    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    public String getIdFavorecido() {
        return idFavorecido;
    }
    public void setIdFavorecido(String idFavorecido) {
        this.idFavorecido = idFavorecido;
    }
    public void setNomeFavorecido(String nomeFavorecido) {
        this.nomeFavorecido = nomeFavorecido;
    }
    public void setDocFavorecido(String docFavorecido) {
        this.docFavorecido = docFavorecido;
    }
    public Alvara getAlvara() {
        return alvara;
    }
    public void setAlvara(Alvara alvara) {
        this.alvara = alvara;
    }
    public void setConta(Conta conta) {
        this.conta = conta;
    }
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    public void setComAtualizacao(Boolean comAtualizacao) {
        this.comAtualizacao = comAtualizacao;
    }
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    public void setContaJudicial(ContaJudicial contaJudicial) {
        this.contaJudicial = contaJudicial;
    }
    public String getNomeSacador() {
        return nomeSacador;
    }
    public void setNomeSacador(String nomeSacador1) {
        this.nomeSacador = nomeSacador1;
    }
    public String getDocumentoSacador() {
        return documentoSacador;
    }
    public void setDocumentoSacador(String documentoSacador1) {
        this.documentoSacador = documentoSacador1;
    }
    public String getNomeSacador2() {
        return nomeSacador2;
    }
    public void setNomeSacador2(String nomeSacador2) {
        this.nomeSacador2 = nomeSacador2;
    }
    public String getDocumentoSacador2() {
        return documentoSacador2;
    }
    public void setDocumentoSacador2(String documentoSacador2){
        this.documentoSacador2 = documentoSacador2;
    }
    public Integer getValidade() { return validade; }
    public void setValidade(Integer validade) {
        this.validade = validade;
    }

    public GerarAlvara21Request gerarAlvaraRequest(Long idOrgaoJulgador, Requisitante autorizador, int validadeAlvara) {
        Requisitante requisitante = new Requisitante(alvara.getRequisitante(), alvara.getCpfRequisitante());
        return new GerarAlvara21Request(this, idOrgaoJulgador, alvara.getMinuta().getProcesso(), requisitante, autorizador, validadeAlvara, alvara.getDataCadastro());
    }

    public GerarAlvara27Request gerarAlvara27Request(Long idOrgaoJulgador) {
        Requisitante requisitante = new Requisitante(alvara.getRequisitante(), alvara.getCpfRequisitante());
        return new GerarAlvara27Request(this, idOrgaoJulgador, alvara.getMinuta().getProcesso(), requisitante, alvara.getDataCadastro());
    }

    public GerarDepositoRequest gerarDepositoRequest(Long idOrgaoJulgador, Conta contaCentralizadora) {
        return new GerarDepositoRequest(this, idOrgaoJulgador, contaCentralizadora);
    }
}
