package br.jus.tjro.gabinete.model.gab.endereco;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.*;

@Entity
@Table(name = "ENDERECO")
@SequenceGenerator(name = Endereco.SEQUENCE_NAME, sequenceName = Endereco.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class Endereco {

    static final String SEQUENCE_NAME = "SEQ_ENDERECO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "id_endereco_legado")
    private Long idEnderecoLegado;

    @Column(length = 200)
    private String logradouro;

    @Column(length = 15)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String numero;

    @Column(length = 100)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String complemento;

    @Column(length = 100)
    private String bairro;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_cep", foreignKey = @ForeignKey(name = "FK_ENDERECO_ENDERECO_CEP"))
    private EnderecoCep cep;

    @Transient
    private String enderecoCompleto;

    public Endereco() {
    }

    public Endereco(Long idEnderecoLegado, String logradouro, String numero, String complemento, String bairro,
                    EnderecoCep cep) {
        this.idEnderecoLegado = idEnderecoLegado;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cep = cep;
        this.enderecoCompleto = getEnderecoCompleto();
    }

    public String getEnderecoCompleto() {

        String endCompleto = "";

        if (logradouro != null)
            endCompleto = this.logradouro.toUpperCase();

        if (this.numero != null) {
            endCompleto += " " + this.numero;
        }
        if (this.complemento != null) {
            endCompleto += ", " + this.complemento.toUpperCase();
        }
        if (this.bairro != null) {
            endCompleto += " " + this.bairro.toUpperCase();
        }
        if (this.cep != null && this.cep.getNumero() != null) {
            endCompleto += " - " + this.cep.getNumero();
        }

        if (this.cep != null && this.cep.getMunicipio() != null) {
            endCompleto += " - " + this.cep.getMunicipio().getDescricao().toUpperCase();
        }

        if (this.cep != null && this.cep.getMunicipio().getEstado() != null) {
            endCompleto += " - " + this.cep.getMunicipio().getEstado().getDescricao().toUpperCase();
        }
        return endCompleto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEnderecoLegado() {
        return idEnderecoLegado;
    }

    public void setIdEnderecoLegado(Long idEnderecoLegado) {
        this.idEnderecoLegado = idEnderecoLegado;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public EnderecoCep getCep() {
        return cep;
    }

    public void setCep(EnderecoCep cep) {
        this.cep = cep;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }
}
