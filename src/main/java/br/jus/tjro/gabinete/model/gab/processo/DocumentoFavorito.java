package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "documento_favorito")
@SequenceGenerator(name = DocumentoFavorito.SEQUENCE_NAME, sequenceName = DocumentoFavorito.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class DocumentoFavorito {

    public static final String SEQUENCE_NAME = "SEQUENCIA_DOCUMENTO_FAVORITO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "id_usuario")
    @NotNull
    private String idUsuario;

    @Column(name = "id_processo")
    @NotNull
    private Long idProcesso;

    @Column(name = "numero_pagina")
    @NotNull
    private int numeroPagina;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @Column(name = "fonte_dados")
    @Enumerated(EnumType.STRING)
    private FonteDadosEnum fonteDados;

    @ManyToOne
    @JoinColumn(name = "id_documento", foreignKey = @ForeignKey(name = "FK_DOCUMENTO_FAV_PROCESSO_DOC"))
    private ProcessoDocumento documento;

    public DocumentoFavorito() {
    }

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

    public Long getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(Long idProcesso) {
        this.idProcesso = idProcesso;
    }

    public int getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public FonteDadosEnum getFonteDados() {
        return fonteDados;
    }

    public void setFonteDados(FonteDadosEnum fonteDados) {
        this.fonteDados = fonteDados;
    }

    public ProcessoDocumento getDocumento() {
        return documento;
    }

    public void setDocumento(ProcessoDocumento documento) {
        this.documento = documento;
    }
}
