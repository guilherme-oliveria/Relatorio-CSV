package br.jus.tjro.gabinete.model.gab.localizador;

public class CaixaWrapper {

    private int id;
    private String nome;

    public CaixaWrapper(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
