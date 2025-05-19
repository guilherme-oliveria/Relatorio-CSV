package br.jus.tjro.gabinete.model.gab.transiente;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Deprecated
public class ParteEnderecoTransiente {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_processo_parte")
    private Partes parte;

    @ManyToOne
    private EnderecoTransiente endereco;

    public ParteEnderecoTransiente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partes getParte() {
        return parte;
    }

    public void setParte(Partes parte) {
        this.parte = parte;
    }

    public EnderecoTransiente getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoTransiente endereco) {
        this.endereco = endereco;
    }
}

