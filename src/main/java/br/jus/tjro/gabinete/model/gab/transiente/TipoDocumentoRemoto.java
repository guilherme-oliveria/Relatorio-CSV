package br.jus.tjro.gabinete.model.gab.transiente;

@Deprecated
public class TipoDocumentoRemoto {

    private Long id;

    private String descricao;

    private boolean ativo;

    private boolean inTipoDocumento;

    private boolean inPublico;

    private Character inTipoComunicacao;

    private Character inTipoExpediente;

    private Character tpVisibilidade;

    public TipoDocumentoRemoto() {
    }

    public TipoDocumentoRemoto(Long id) {
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
