package br.jus.tjro.gabinete.model.gab.alvara;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "CONTA_JUDICIAL")
@SequenceGenerator(name = ContaJudicial.SEQUENCE_NAME, sequenceName = ContaJudicial.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class ContaJudicial {
    public static final String SEQUENCE_NAME = "SEQ_CONTA_JUDICIAL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "nome_beneficiario")
    private String nomeBeneficiario;

    @Column(name = "cod_legado")
    private String codLegado;

    @NotNull
    private String agencia;

    @Column(name = "numero_conta")
    @NotNull
    private String numeroConta;

    @Column(name = "digito_verificador")
    @NotNull
    private String digitoVerificador;

    @Column(name = "cod_banco")
    private String codBanco;

    private String produto;

    @Column(name = "valor_guia")
    private Double valorGuia;

    @Column(name = "valor_atualizado")
    private Double valorAtualizado;

    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @Transient
    private Saldo saldo;

    @Transient
    private String dataPagamentoBoleto;

    @Transient
    private Optional<OrigemDepositoEnum> origemDeposito;

    public ContaJudicial(){

    }

    public ContaJudicial(String nomeBeneficiario, String codLegado, String agencia, String numeroConta,
                         String digitoVerificador, String codBanco, String produto, Double valorGuia,
                         Double valorAtualizado, Saldo saldo, String dataPagamentoBoleto, Optional<OrigemDepositoEnum> origemDeposito) {
        this(codLegado,agencia,numeroConta,digitoVerificador,codBanco,produto,valorGuia, dataPagamentoBoleto, origemDeposito);
        this.nomeBeneficiario = nomeBeneficiario;
        this.valorAtualizado = valorAtualizado;
        this.saldo = saldo;
    }

    public ContaJudicial(@JsonProperty("id") String codLegado, @JsonProperty("agencia")  String agencia,
                         @JsonProperty("numeroConta")  String numeroConta, @JsonProperty("digitoVerificador") String digitoVerificador,
                         @JsonProperty("codBanco") String codBanco, @JsonProperty("produto") String produto,
                         @JsonProperty("valorGuia") Double valorGuia, @JsonProperty("dataPagamentoBoleto") String dataPagamentoBoleto,
                         @JsonProperty(value = "origemDeposito") Optional<OrigemDepositoEnum> origemDeposito) {
        this.codLegado = codLegado;
        this.agencia = agencia;
        this.numeroConta = numeroConta;
        this.digitoVerificador = digitoVerificador;
        this.codBanco = codBanco;
        this.produto = produto;
        this.valorGuia = valorGuia;
        this.dataPagamentoBoleto = dataPagamentoBoleto;
        this.origemDeposito = origemDeposito;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContaJudicial that = (ContaJudicial) o;
        return Objects.equals(nomeBeneficiario, that.nomeBeneficiario) &&
            Objects.equals(agencia, that.agencia) &&
            Objects.equals(numeroConta, that.numeroConta) &&
            Objects.equals(digitoVerificador, that.digitoVerificador) &&
            Objects.equals(codBanco, that.codBanco) &&
            Objects.equals(produto, that.produto) &&
            Objects.equals(valorGuia, that.valorGuia) &&
            Objects.equals(valorAtualizado, that.valorAtualizado) &&
            Objects.equals(dataAtualizacao, that.dataAtualizacao) &&
            Objects.equals(saldo, that.saldo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomeBeneficiario, agencia, numeroConta, digitoVerificador, codBanco, produto, valorGuia, valorAtualizado, dataAtualizacao, saldo);
    }

    public String getContaCorrenteComDigito() {
        return this.getNumeroConta() + "-" + this.getDigitoVerificador();
    }

    public String getValorGuiaFormatado() {
        if (this.getValorGuia() == null) return "0,00";
        return formataValor(this.getValorGuia());
    }

    public String getValorAtualizadoFormatado() {
        if (this.getValorAtualizado() == null) return "0,00";
        return formataValor(this.getValorAtualizado());
    }

    private String formataValor(Double valor) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeBeneficiario() {
        return nomeBeneficiario;
    }

    public void setNomeBeneficiario(String nomeBeneficiario) {
        this.nomeBeneficiario = nomeBeneficiario;
    }

    public String getCodLegado() {
        return codLegado;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public String getDigitoVerificador() {
        return digitoVerificador;
    }

    public String getCodBanco() {
        return codBanco;
    }

    public String getProduto() {
        return produto;
    }

    public Double getValorGuia() {
        return valorGuia;
    }

    public Double getValorAtualizado() {
        return valorAtualizado;
    }

    public void setValorAtualizado(Double valorAtualizado) {
        this.valorAtualizado = valorAtualizado;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Saldo getSaldo() {
        return saldo;
    }

    public void setSaldo(Saldo saldo) {
        this.saldo = saldo;
    }

    public String getDataPagamentoBoleto() {
        return dataPagamentoBoleto;
    }

    public void setDataPagamentoBoleto(String dataPagamentoBoleto) {
        this.dataPagamentoBoleto = dataPagamentoBoleto;
    }

    public Optional<OrigemDepositoEnum> getOrigemDeposito() {
        return origemDeposito;
    }

    public void setOrigemDeposito(Optional<OrigemDepositoEnum> origemDeposito) {
        this.origemDeposito = origemDeposito;
    }
}
