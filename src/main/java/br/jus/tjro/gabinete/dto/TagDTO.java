package br.jus.tjro.gabinete.dto;

import java.util.Date;

public class TagDTO {

    private String nome;

    private String excluido = "";

    private String idTagPje = "";

    public TagDTO() {
    }
    public TagDTO(String nome, Date dataExclusaoProcessoTag, Integer idTagPje) {
        this.nome = nome;
        if(dataExclusaoProcessoTag !=null){
            this.excluido = "true";
        }
        if(idTagPje != null)
            this.idTagPje = idTagPje.toString();
    }

    public String getIdTagPje() {
        return idTagPje;
    }

    public void setIdTagPje(String idTagPje) {
        this.idTagPje = idTagPje;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getExcluido() {
        return excluido;
    }

    public void setExcluido(String excluido) {
        this.excluido = excluido;
    }
}
