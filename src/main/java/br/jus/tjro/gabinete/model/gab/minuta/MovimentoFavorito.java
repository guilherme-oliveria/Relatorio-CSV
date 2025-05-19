package br.jus.tjro.gabinete.model.gab.minuta;



import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;

@Entity
@Table(name = "MOVIMENTO_FAVORITO", uniqueConstraints = {
    @UniqueConstraint(name = "UK_ID_MOVIMENTO_LOGIN_USUARIO",columnNames = {"id_movimento", "login_usuario"})})
public class MovimentoFavorito implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MovimentoFavoritoPK id;

    public MovimentoFavorito() {
    }

    public MovimentoFavorito(MovimentoFavoritoPK id) {
        this.id = id;
    }

    public MovimentoFavoritoPK getId() {
        return id;
    }

    public void setId(MovimentoFavoritoPK id) {
        this.id = id;
    }
}
