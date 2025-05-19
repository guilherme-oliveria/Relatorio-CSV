package br.jus.tjro.gabinete.dto;

import java.util.Date;
@Deprecated
public class MovimentoProcessoDTO {

    private Long id;
    private Long idProcessoMovimentoLegado;
    private String descricao;
    private Date data;
    private String cpfUsuario;
    private String nomeUsuario;
    private Long cdEvento;
    private Long processoDocumentoId;

    public MovimentoProcessoDTO() {
    }

    public MovimentoProcessoDTO(Long id, Long idProcessoMovimentoLegado, String descricao, Date data, String cpfUsuario, String nomeUsuario, Long cdEvento, Long processoDocumentoId) {
        this.id = id;
        this.idProcessoMovimentoLegado = idProcessoMovimentoLegado;
        this.descricao = descricao;
        this.data = data;
        this.cpfUsuario = cpfUsuario;
        this.nomeUsuario = nomeUsuario;
        this.cdEvento = cdEvento;
        this.processoDocumentoId = processoDocumentoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProcessoMovimentoLegado() {
        return idProcessoMovimentoLegado;
    }

    public void setIdProcessoMovimentoLegado(Long idProcessoMovimentoLegado) {
        this.idProcessoMovimentoLegado = idProcessoMovimentoLegado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
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

    public Long getCdEvento() {
        return cdEvento;
    }

    public void setCdEvento(Long cdEvento) {
        this.cdEvento = cdEvento;
    }

    public Long getProcessoDocumentoId() {
        return processoDocumentoId;
    }

    public void setProcessoDocumentoId(Long processoDocumentoId) {
        this.processoDocumentoId = processoDocumentoId;
    }
}
