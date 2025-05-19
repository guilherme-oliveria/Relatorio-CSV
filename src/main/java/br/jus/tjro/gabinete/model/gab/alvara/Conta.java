package br.jus.tjro.gabinete.model.gab.alvara;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "conta")
@SequenceGenerator(name = Conta.SEQUENCE_NAME, sequenceName = Conta.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class Conta {
    public static final String SEQUENCE_NAME = "SEQ_CONTA";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    private String banco;

    private String agencia;

    private String operacao;

    @Column(name = "numero_conta")
    private String numeroConta;

    @Column(name = "digito_verificador")
    private String digitoVerificador;

    @Transient
    private String numeroProcesso;

    @Transient
    private Long idLegado;

    public Conta(){

    }

    public Conta(@JsonProperty("banco") String banco, @JsonProperty("agencia") String agencia,
                 @JsonProperty("operacao") String operacao, @JsonProperty("numeroConta") String numeroConta,
                 @JsonProperty("digitoVerificador") String digitoVerificador,
                 @JsonProperty("numeroProcesso") String numeroProcesso,
                 @JsonProperty("idLegado") Long idLegado) {
        this.banco = banco;
        this.agencia = agencia;
        this.operacao = operacao;
        this.numeroConta = numeroConta;
        this.digitoVerificador = digitoVerificador;
        this.numeroProcesso = numeroProcesso;
        this.idLegado = idLegado;
    }

    public boolean isContaCentralizadora(Conta contaCentralizadora) {
        return this.numeroConta.equals(contaCentralizadora.getNumeroConta())
            && this.agencia.equals(contaCentralizadora.getAgencia())
            && this.banco.equals(contaCentralizadora.getBanco())
            && this.digitoVerificador.equals(contaCentralizadora.getDigitoVerificador());
    }

    public String getContaCorrenteComDigito() {
        return this.getNumeroConta() + "-" + this.getDigitoVerificador();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBanco() {
        return banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public String getDigitoVerificador() {
        return digitoVerificador;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public Long getIdLegado() {
        return idLegado;
    }

    public void setIdLegado(Long idLegado) {
        this.idLegado = idLegado;
    }

    public boolean isDefault() {
        return this.getBanco().equals("Default");
    }
}
