package br.jus.tjro.gabinete.dto;

import java.util.ArrayList;
import java.util.List;

public class TagProcessoDTO {

    private String idProcesso;

    private List<TagDTO> tagsList;

    public TagProcessoDTO() {
    }

    public TagProcessoDTO(String idProcesso) {
        this.idProcesso = idProcesso;
    }

    public String getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(String idProcesso) {
        this.idProcesso = idProcesso;
    }

    public List<TagDTO> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<TagDTO> tagsList) {
        this.tagsList = tagsList;
    }

    public void addAllNomeTags(List<TagDTO> nomeTags) {
        if(this.tagsList==null){
            this.tagsList = new ArrayList<>();
        }
        this.tagsList.addAll(nomeTags);
    }
}
