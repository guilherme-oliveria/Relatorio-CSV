package br.jus.tjro.gabinete.model.gab;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Deprecated
public class TipoAnexo {

    private Long id;

    private String descricao;

    private boolean ativo;

    private Boolean minuta;

    private boolean inTipoDocumento;

    private boolean inPublico;

    private Character inTipoComunicacao;

    private Character inTipoExpediente;

    private Character tpVisibilidade;

    public TipoAnexo() {
    }

    public TipoAnexo(
        Long id, String descricao, boolean ativo, boolean inTipoDocumento, boolean inPublico,
        Character inTipoComunicacao, Character inTipoExpediente, Character tpVisibilidade) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = ativo;
        this.minuta = false;
        this.inTipoDocumento = inTipoDocumento;
        this.inPublico = inPublico;
        this.inTipoComunicacao = inTipoComunicacao;
        this.inTipoExpediente = inTipoExpediente;
        this.tpVisibilidade = tpVisibilidade;
    }

    public TipoAnexo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean isMinuta() {
        if (minuta == null)
            return false;
        return minuta;
    }

    public void setMinuta(Boolean minuta) {
        this.minuta = minuta;
    }

    public boolean isInTipoDocumento() {
        return inTipoDocumento;
    }

    public void setInTipoDocumento(boolean inTipoDocumento) {
        this.inTipoDocumento = inTipoDocumento;
    }

    public boolean isInPublico() {
        return inPublico;
    }

    public void setInPublico(boolean inPublico) {
        this.inPublico = inPublico;
    }

    public Character getInTipoComunicacao() {
        return inTipoComunicacao;
    }

    public void setInTipoComunicacao(Character inTipoComunicacao) {
        this.inTipoComunicacao = inTipoComunicacao;
    }

    public Character getInTipoExpediente() {
        return inTipoExpediente;
    }

    public void setInTipoExpediente(Character inTipoExpediente) {
        this.inTipoExpediente = inTipoExpediente;
    }

    public Character getTpVisibilidade() {
        return tpVisibilidade;
    }

    public void setTpVisibilidade(Character tpVisibilidade) {
        this.tpVisibilidade = tpVisibilidade;
    }
}
