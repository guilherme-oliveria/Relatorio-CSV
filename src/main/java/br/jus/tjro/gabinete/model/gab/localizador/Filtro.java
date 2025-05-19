package br.jus.tjro.gabinete.model.gab.localizador;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "FILTRO")
@SequenceGenerator(name = Filtro.SEQUENCE_NAME, sequenceName = Filtro.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class Filtro {

    public static final String SEQUENCE_NAME = "SEQ_FILTRO";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "COR")
    private String cor;
    @Lob
    @Column(name = "PARAMETRO")
    private String parametro;


    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    private List<LocalizadorOrgaoJulgador> orgaosJulgadores;

    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(
        name = "FILTRO_LOC_USUARIO",
        joinColumns =
        @JoinColumn(name = "FILTRO_ID", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_FILT_USU_PRIOR_PROC")),
        inverseJoinColumns =
        @JoinColumn(name = "USUARIOS_ID", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_FILTRO_LOC_USUARIO_LOC_USU"))
    )
    private List<LocalizadorUsuario> usuarios;
    @Transient
    @JsonInclude(Include.NON_EMPTY)
    private Long tamanho;

    public Filtro() {
    }

    public Filtro(Long id) {
        this.id = id;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public List<LocalizadorOrgaoJulgador> getOrgaosJulgadores() {
        return orgaosJulgadores;
    }

    public void setOrgaosJulgadores(List<LocalizadorOrgaoJulgador> orgaosJulgadores) {
        this.orgaosJulgadores = orgaosJulgadores;
    }

    public List<LocalizadorUsuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<LocalizadorUsuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

}
