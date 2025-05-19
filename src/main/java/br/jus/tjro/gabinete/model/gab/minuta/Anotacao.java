package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.model.gab.enums.TipoAnotacao;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ANOTACAO")
@SequenceGenerator(name = Anotacao.SEQUENCE_NAME, sequenceName = Anotacao.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class Anotacao {

    public static final String SEQUENCE_NAME = "SEQUENCIA_ANOTACAO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ANOTACAO")
    @NotNull
    @NotBlank
    private String anotacao;

    @Column(name = "USUARIO_NOME", length = 100)
    @NotNull
    private String usuarioNome;

    @Column(name = "CPF_USUARIO", length = 14)
    @NotNull
    private String cpfUsuario;


    @Column(name = "DATA_CADASTRO")
    @NotNull
    private Date dataCadastro;

    @Column(name = "PROCESSO_ID")
    private Long idProcesso;

    @Enumerated(EnumType.STRING)
    private TipoAnotacao tipo;

    private String publicidade;

    public static List<Anotacao> ordenaPorDataCriacaoMaisRecentePrimeiro(List<Anotacao> anotacoes) {
        Collections.sort(anotacoes, new Comparator<Anotacao>() {
            public int compare(Anotacao a1, Anotacao a2) {
                if (a2.getDataCadastro().equals(a1.getDataCadastro()))
                    return a2.getId().compareTo(a1.getId());
                return a2.getDataCadastro().compareTo(a1.getDataCadastro());
            }
        });
        return anotacoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnotacao() {
        return anotacao;
    }

    public void setAnotacao(String anotacao) {
        this.anotacao = anotacao;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Long getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(Long idProcesso) {
        this.idProcesso = idProcesso;
    }

    public TipoAnotacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoAnotacao tipo) {
        this.tipo = tipo;
    }

    public String getPublicidade() {
        return publicidade;
    }

    public void setPublicidade(String publicidade) {
        this.publicidade = publicidade;
    }
}
