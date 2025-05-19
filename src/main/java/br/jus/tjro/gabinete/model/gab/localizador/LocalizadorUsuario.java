package br.jus.tjro.gabinete.model.gab.localizador;

import br.jus.tjro.gabinete.model.gab.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LOC_USUARIO")
public class LocalizadorUsuario {

    @Id
    @Column(name = "ID")
    private String id;

    public LocalizadorUsuario() {
    }

    public LocalizadorUsuario(String id) {
        this.id = id;
    }

    public LocalizadorUsuario(Usuario usuario) {
        this.id = usuario.getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
