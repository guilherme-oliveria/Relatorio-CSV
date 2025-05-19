package br.jus.tjro.gabinete.model.gab.endereco;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;

@Entity
@Table(name = "ENDERECO_CEP")
@SequenceGenerator(name = EnderecoCep.SEQUENCE_NAME, sequenceName = EnderecoCep.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class EnderecoCep {

    static final String SEQUENCE_NAME = "SEQ_ENDERECO_CEP";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(length = 9, unique = true)
    private String numero;

    @Column(length = 200)
    private String logradouro;

    @Column(length = 100)
    private String bairro;

    @Column(length = 100)
    private String complemento;

    @OneToOne
    @JoinColumn(name = "id_municipio", foreignKey = @ForeignKey(name = "FK_ENDERECO_CEP_ENDERECO_MUN"))
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EnderecoMunicipio municipio;

    public EnderecoCep() {
    }

    public EnderecoCep(String numero, String logradouro, String bairro, String complemento, EnderecoMunicipio municipio) {
        this.numero = numero;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.complemento = complemento;
        this.municipio = municipio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public EnderecoMunicipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(EnderecoMunicipio municipio) {
        this.municipio = municipio;
    }
}
