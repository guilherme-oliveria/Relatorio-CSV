package br.jus.tjro.gabinete.model.webjud;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class OrgaoEndereco {

    public OrgaoEndereco(
        String descricao,
        String complemento,
        String bairro,
        String municipio,
        String numero,
        String uf,
        String cep){
        this.descricao = descricao;
        this.complemento = complemento;
        this.bairro = bairro;
        this.municipio = municipio;
        this.numero = numero;
        this.uf = uf;
        this.cep = cep;
    }

    public OrgaoEndereco() {}

    private Long id;

    private String descricao;

    private String complemento;

    private String bairro;

    private String municipio;

    private String numero;

    private String uf;

    private String cep;

    private String enderecoCompleto;

    @JsonBackReference
    private OrgaoJulgador orgaoJulgador;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEnderecoCompleto() {
        List<String> end = new ArrayList<>();
        end.add(this.getDescricao());
        if (this.getNumero() != null)
            end.add("nº " + this.getNumero());

        if (this.getBairro() != null)
            end.add("Bairro " + this.getBairro());

        if (this.getCep() != null)
            end.add("CEP " + this.getCep());

        if (this.getMunicipio() != null)
            end.add(this.getMunicipio());

        if (this.getUf() != null)
            end.add(this.getUf());

        if(this.getComplemento() != null && !this.getComplemento().equals(""))
            end.add(this.getComplemento());

        return String.join(", ", end);
    }

    public OrgaoJulgador getOrgaoJulgador() {
        return orgaoJulgador;
    }

    public void setOrgaoJulgador(OrgaoJulgador orgaoJulgador) {
        this.orgaoJulgador = orgaoJulgador;
    }
}
