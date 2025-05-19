package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.model.gab.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MovimentoFavoritoPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "login_usuario")
    private String loginUsuario;

    @Column(name = "id_movimento")
    private Long codigoMovimento;

    public MovimentoFavoritoPK() {
    }

    public MovimentoFavoritoPK(Usuario usuario, Long codigoMovimento) {
        super();
        this.loginUsuario = usuario.getId();
        this.codigoMovimento = codigoMovimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovimentoFavoritoPK that = (MovimentoFavoritoPK) o;
        return Objects.equals(loginUsuario, that.loginUsuario) &&
            Objects.equals(codigoMovimento, that.codigoMovimento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loginUsuario, codigoMovimento);
    }

    public String getLoginUsuario() {
        return loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public Long getCodigoMovimento() {
        return codigoMovimento;
    }

    public void setCodigoMovimento(Long codigoMovimento) {
        this.codigoMovimento = codigoMovimento;
    }
}
