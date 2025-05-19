package br.jus.tjro.gabinete.model.gab.transiente;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Deprecated
public class EnderecoTransiente {

    @Id
    private Long id;

    private String logradouro;

    private String numero;

    private String complemento;

    private String bairro;

    @OneToOne
    @JoinColumn(name = "id_cep")
    private CepTransiente cep;

    public CepTransiente getCep() {
        return cep;
    }

    public void setCep(CepTransiente cep) {
        this.cep = cep;
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

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
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
}
