package br.jus.tjro.gabinete.model.gab.transiente;

import br.jus.tjro.gabinete.model.gab.endereco.Estado;

import jakarta.persistence.Id;


public class MunicipioTransiente {
    @Id
    private int id;

    private String descricao;

    private Estado estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
