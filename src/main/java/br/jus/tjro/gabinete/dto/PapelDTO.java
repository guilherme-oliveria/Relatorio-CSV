package br.jus.tjro.gabinete.dto;

import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;

import java.util.ArrayList;
import java.util.List;

//Evitar usar DTO, alem do mais o papel no front esta tendo comportamento de recursos;
@Deprecated
public class PapelDTO {
    private String papel;
    private List<String> orgaoJulgadores = new ArrayList<String>();

    public PapelDTO(String papel, List<String> orgaoJulgadores) {
        this.papel = papel;
        this.orgaoJulgadores = orgaoJulgadores;
    }

    public PapelDTO(Papel papel, List<OrgaoJulgador> orgaoJulgadores) {
        this(papel.getRecursosName(),orgaoJulgadores.stream().map(it -> it.getId()).toList());
    }

    public String getPapel() {
        return papel;
    }

    public void setPapel(String papel) {
        this.papel = papel;
    }

    public List<String> getOrgaoJulgadores() {
        return orgaoJulgadores;
    }

    public void setOrgaoJulgadores(List<String> orgaoJulgadores) {
        this.orgaoJulgadores = orgaoJulgadores;
    }
}
