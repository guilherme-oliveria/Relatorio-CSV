package br.jus.tjro.gabinete.model.gab.transiente;

import br.jus.tjro.gabinete.model.gab.endereco.Estado;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Deprecated
public class PessoaDocumentosTransiente {

    @Id
    private Long idPessoaDocIdentificacao;

    private Long idPessoaLegado;

    private String tipoDocumento;

    private String nrDocumento;

    private Boolean ehPrincipal;

    @ManyToOne
    private Estado estado;

    public PessoaDocumentosTransiente() {
    }

    public PessoaDocumentosTransiente(Long idPessoaDocIdentificacao, Long idPessoaLegado, String tipoDocumento,
                                      String nrDocumento, Boolean ehPrincipal, Estado estado) {
        this.idPessoaDocIdentificacao = idPessoaDocIdentificacao;
        this.idPessoaLegado = idPessoaLegado;
        this.tipoDocumento = tipoDocumento;
        this.nrDocumento = nrDocumento;
        this.ehPrincipal = ehPrincipal;
        this.estado = estado;
    }

    public Long getIdPessoaDocIdentificacao() {
        return idPessoaDocIdentificacao;
    }

    public void setIdPessoaDocIdentificacao(Long idPessoaDocIdentificacao) {
        this.idPessoaDocIdentificacao = idPessoaDocIdentificacao;
    }

    public Long getIdPessoaLegado() {
        return idPessoaLegado;
    }

    public void setIdPessoaLegado(Long idPessoaLegado) {
        this.idPessoaLegado = idPessoaLegado;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNrDocumento() {
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public Boolean getEhPrincipal() {
        return ehPrincipal;
    }

    public void setEhPrincipal(Boolean ehPrincipal) {
        this.ehPrincipal = ehPrincipal;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
