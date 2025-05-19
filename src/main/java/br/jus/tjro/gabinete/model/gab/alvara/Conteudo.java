package br.jus.tjro.gabinete.model.gab.alvara;

import java.util.HashMap;

public class Conteudo {
    private Long id;
    private Long codCNJ;
    private String nome;
    private HashMap<String,String> sistemas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodCNJ() {
        return codCNJ;
    }

    public void setCodCNJ(Long codCNJ) {
        this.codCNJ = codCNJ;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public HashMap<String, String> getSistemas() {
        return sistemas;
    }

    public void setSistemas(HashMap<String, String> sistemas) {
        this.sistemas = sistemas;
    }
}
