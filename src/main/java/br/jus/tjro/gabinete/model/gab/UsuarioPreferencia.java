package br.jus.tjro.gabinete.model.gab;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "USUARIO_PREFERENCIA")
@SequenceGenerator(name = UsuarioPreferencia.SEQUENCE_NAME, sequenceName = UsuarioPreferencia.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class UsuarioPreferencia {

    public static final String SEQUENCE_NAME = "SEQUENCIA_USUARIO_PREFERENCIA";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Long id;

    @Column(name = "id_usuario")
    @NotNull
    private String idUsuario;

    @Column(name = "preferencia", length = 50)
    @NotNull
    private String preferencia;

    @Column(name = "valor", length = 100)
    private String valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPreferencia() {
        return preferencia;
    }

    public void setPreferencia(String preferencia) {
        this.preferencia = preferencia;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
