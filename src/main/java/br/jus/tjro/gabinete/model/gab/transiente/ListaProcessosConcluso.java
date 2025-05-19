package br.jus.tjro.gabinete.model.gab.transiente;

import java.util.Date;

public class ListaProcessosConcluso {
    private int idProcesso;

    private String nrProcesso;

    private int idOrgaoJulgador;

    private String orgaoJulgador;

    private int idClasseJudicial;

    private String classeJudicial;

    private Date dtInicio;

    public int getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(int idProcesso) {
        this.idProcesso = idProcesso;
    }

    public String getNrProcesso() {
        return nrProcesso;
    }

    public void setNrProcesso(String nrProcesso) {
        this.nrProcesso = nrProcesso;
    }

    public int getIdOrgaoJulgador() {
        return idOrgaoJulgador;
    }

    public void setIdOrgaoJulgador(int idOrgaoJulgador) {
        this.idOrgaoJulgador = idOrgaoJulgador;
    }

    public int getIdClasseJudicial() {
        return idClasseJudicial;
    }

    public void setIdClasseJudicial(int idClasseJudicial) {
        this.idClasseJudicial = idClasseJudicial;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public String getOrgaoJulgador() {
        return orgaoJulgador;
    }

    public void setOrgaoJulgador(String orgaoJulgador) {
        this.orgaoJulgador = orgaoJulgador;
    }

    public String getClasseJudicial() {
        return classeJudicial;
    }

    public void setClasseJudicial(String classeJudicial) {
        this.classeJudicial = classeJudicial;
    }


}
