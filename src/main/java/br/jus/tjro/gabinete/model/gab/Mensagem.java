package br.jus.tjro.gabinete.model.gab;

import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "Mensagem")
@SequenceGenerator(name = Mensagem.SEQUENCE_NAME, sequenceName = Mensagem.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class Mensagem {

    public static final String SEQUENCE_NAME = "SEQUENCIA_MENSAGEM";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Long id;

    @Column(name = "OBJETO")
    private String objeto;

    @Column(name = "TITULO", length = 100)
    @NotNull
    private String titulo;

    @Column(name = "MENSAGEM", length = 500)
    @NotNull
    private String mensagem;

    @Column(name = "DATA_LIMITE")
    private Timestamp dataLimite;

    @Column(name = "USUARIO_ID", length = 10)
    private String usuarioId;

    @Column(name = "ORGAO_JULGADOR_ID", length = 10)
    private String orgaoJulgadorId;

    @Column(name = "DATA_CADASTRO")
    private Timestamp dataCadastro;

    @Column(name = "EXCLUIR_APOS_NOTIFICACAO")
    private Boolean excluirAposNotificacao;

    @Column(name = "VISUALIZADO")
    private Boolean visualizado;

    @Column(name = "ENVIADO")
    private Boolean enviado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
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

    public Timestamp getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(Timestamp dataLimite) {
        this.dataLimite = dataLimite;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getOrgaoJulgadorId() {
        return orgaoJulgadorId;
    }

    public void setOrgaoJulgadorId(String orgaoJulgadorId) {
        this.orgaoJulgadorId = orgaoJulgadorId;
    }

    public Timestamp getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Timestamp dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getExcluirAposNotificacao() {
        return excluirAposNotificacao;
    }

    public void setExcluirAposNotificacao(Boolean excluirAposNotificacao) {
        this.excluirAposNotificacao = excluirAposNotificacao;
    }

    public Boolean getVisualizado() {
        return visualizado;
    }

    public void setVisualizado(Boolean visualizado) {
        this.visualizado = visualizado;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }
}
