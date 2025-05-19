package br.jus.tjro.gabinete.model.gab.minuta;

import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "AUTOTEXTO")
@SequenceGenerator(name = AutoTexto.SEQUENCE_NAME, sequenceName = AutoTexto.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class AutoTexto {

    public static final String SEQUENCE_NAME = "SEQUENCIA_AUTOTEXTO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TEXTO")
    @NotNull
    private String texto;

    @Column(name = "TITULO")
    @NotNull
    private String titulo;

    @Column(name = "ID_USUARIO")
    @NotNull
    private String idUsuario;

    @Column(name = "ID_ORGAO_JULGADOR_STR")
    @NotNull
    private String idOrgaoJulgador;

    @Column(name = "PUBLICIDADE", length = 20)
    @NotNull
    private String publicidade;

    @Column(name = "DATA_EXCLUSAO")
    private Date dataExclusao;

    @Column(name = "ID_USUARIO_EXCLUSAO")
    private String idUsuarioExclusao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdOrgaoJulgador() {
        return idOrgaoJulgador;
    }

    public void setIdOrgaoJulgador(String idOrgaoJulgador) {
        this.idOrgaoJulgador = idOrgaoJulgador;
    }

    public String getPublicidade() {
        return publicidade;
    }

    public void setPublicidade(String publicidade) {
        this.publicidade = publicidade;
    }

    public Date getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(Date dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public String getIdUsuarioExclusao() {
        return idUsuarioExclusao;
    }

    public void setIdUsuarioExclusao(String idUsuarioExclusao) {
        this.idUsuarioExclusao = idUsuarioExclusao;
    }
}
