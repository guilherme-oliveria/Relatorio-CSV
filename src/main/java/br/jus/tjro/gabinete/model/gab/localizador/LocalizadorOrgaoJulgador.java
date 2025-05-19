package br.jus.tjro.gabinete.model.gab.localizador;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LOC_ORGAO_JULGADOR")
public class LocalizadorOrgaoJulgador {


    @Id
    @Column(name = "ID_STR")
    private String id;

    public LocalizadorOrgaoJulgador() {
    }

    public LocalizadorOrgaoJulgador(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
