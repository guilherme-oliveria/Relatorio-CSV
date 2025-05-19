package br.jus.tjro.gabinete.model.gab.transiente;

import br.jus.tjro.gabinete.model.gab.enums.TipoComplementoEnum;

public class TipoComplemento {
    private Long id;
    private String nome;
    private String observacao;

    public TipoComplemento() {

    }

    public TipoComplemento(Long id, String nome, String observacao) {
        this.id = id;
        this.nome = nome;
        this.observacao = observacao;
    }

    public TipoComplemento(Long id, TipoComplementoEnum nome, String observacao) {
        this.id = id;
        this.nome = nome.tipo;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
