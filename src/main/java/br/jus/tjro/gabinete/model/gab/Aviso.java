package br.jus.tjro.gabinete.model.gab;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "AVISO")
@SequenceGenerator(name = Aviso.SEQUENCE_NAME, sequenceName = Aviso.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Aviso {

    public static final String SEQUENCE_NAME = "SEQUENCIA_AVISO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;
    private Date dtCadastro;
    private Date dtIniVisibilidade;
    private Date dtFimVisibilidade;
    private String cpfUsuario;
    private String nomeUsuario;
    private String titulo;
    private String mensagem;
    private Boolean inAtivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public Date getDtIniVisibilidade() {
        return dtIniVisibilidade;
    }

    public void setDtIniVisibilidade(Date dtIniVisibilidade) {
        this.dtIniVisibilidade = dtIniVisibilidade;
    }

    public Date getDtFimVisibilidade() {
        return dtFimVisibilidade;
    }

    public void setDtFimVisibilidade(Date dtFimVisibilidade) {
        this.dtFimVisibilidade = dtFimVisibilidade;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean getInAtivo() {
        return inAtivo;
    }

    public void setInAtivo(Boolean inAtivo) {
        this.inAtivo = inAtivo;
    }
}
