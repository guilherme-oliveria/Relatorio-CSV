package br.jus.tjro.gabinete.model.gab.transiente;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Deprecated
public class CepTransiente {

    @Id
    private int id;

    private String numero;

    private String logradouro;

    private String bairro;

    private String complemento;

    @OneToOne
    @JoinColumn(name = "id_municipio")
    private MunicipioTransiente municipio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public MunicipioTransiente getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioTransiente municipio) {
        this.municipio = municipio;
    }
}
