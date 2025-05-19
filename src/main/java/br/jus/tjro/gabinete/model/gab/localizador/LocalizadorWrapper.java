package br.jus.tjro.gabinete.model.gab.localizador;

import java.util.List;


public class LocalizadorWrapper {

    private String nome;
    private String orgaoJulgador;
    private List<RegraWrapper> regras;
    private boolean exclusivo;
    private boolean deOrgaoJulgador;
    private Long id;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getOrgaoJulgador() {
        return orgaoJulgador;
    }

    public void setOrgaoJulgador(String orgaoJulgador) {
        this.orgaoJulgador = orgaoJulgador;
    }

    public List<RegraWrapper> getRegras() {
        return regras;
    }

    public void setRegras(List<RegraWrapper> regras) {
        this.regras = regras;
    }

    public boolean isExclusivo() {
        return exclusivo;
    }

    public void setExclusivo(boolean exclusivo) {
        this.exclusivo = exclusivo;
    }

    public boolean isDeOrgaoJulgador() {
        return deOrgaoJulgador;
    }

    public void setDeOrgaoJulgador(boolean deOrgaoJulgador) {
        this.deOrgaoJulgador = deOrgaoJulgador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean validar() {
        if (nome == null || nome.isEmpty()) {
            return false;
        }
        if (orgaoJulgador == null) {
            return false;
        }
        if (regras == null) {
            return false;
        }
        for (RegraWrapper regra : regras) {
            if (!regra.validar()) {
                return false;
            }
        }
        return true;
    }
}
