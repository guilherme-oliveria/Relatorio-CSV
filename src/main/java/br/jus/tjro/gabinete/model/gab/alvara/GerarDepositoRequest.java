package br.jus.tjro.gabinete.model.gab.alvara;

import java.math.BigDecimal;

public class GerarDepositoRequest {
    private String nomeAutor;
    private TipoPessoa tipoPessoaAutor;
    private String documentoAutor;
    private String nomeReu;
    private TipoPessoa tipoPessoaReu;
    private String documentoReu;
    private TipoDepositante tipoDepositante;
    private String nomeDepositante;
    private TipoPessoa tipoPessoaDepositante;
    private String documentoDepositante;

    //advogado
    private String nomeAdvogadoAutor;
    private TipoPessoa tipoPessoaAdvogadoAutor;
    private String documentoAdvogadoAutor;
    private String nomeAdvogadoReu;
    private TipoPessoa tipoPessoaAdvogadoReu;
    private String documentoAdvogadoReu;
    private BigDecimal valorTotal;
    private String numeroProcesso;
    private Long orgaoId;
    private Integer codComarca;
    private String observacoes;
    private String dataVencimento;
    private Long contaDestinoId;
    private Long numeroGuia;
    private Long idDeposito;
    private Requisitante requisitante;

    public GerarDepositoRequest(PagamentoAlvara pagamentoAlvara, Long orgaoJulgadorDepara, Conta contaCentralizadora) {
        orgaoId = orgaoJulgadorDepara;
        numeroProcesso = contaCentralizadora.getNumeroProcesso();
        nomeAutor = pagamentoAlvara.getNomeFavorecido();
        nomeReu = pagamentoAlvara.getNomeFavorecido();
        valorTotal = pagamentoAlvara.getValor();
        tipoDepositante = TipoDepositante.AUTOR;
        nomeDepositante = pagamentoAlvara.getNomeFavorecido();
        tipoPessoaDepositante = TipoPessoa.PESSOA_JURIDICA;
        documentoDepositante = pagamentoAlvara.getDocFavorecido();
        contaDestinoId = contaCentralizadora.getIdLegado();
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public TipoPessoa getTipoPessoaAutor() {
        return tipoPessoaAutor;
    }

    public void setTipoPessoaAutor(TipoPessoa tipoPessoaAutor) {
        this.tipoPessoaAutor = tipoPessoaAutor;
    }

    public String getDocumentoAutor() {
        return documentoAutor;
    }

    public void setDocumentoAutor(String documentoAutor) {
        this.documentoAutor = documentoAutor;
    }

    public String getNomeReu() {
        return nomeReu;
    }

    public void setNomeReu(String nomeReu) {
        this.nomeReu = nomeReu;
    }

    public TipoPessoa getTipoPessoaReu() {
        return tipoPessoaReu;
    }

    public void setTipoPessoaReu(TipoPessoa tipoPessoaReu) {
        this.tipoPessoaReu = tipoPessoaReu;
    }

    public String getDocumentoReu() {
        return documentoReu;
    }

    public void setDocumentoReu(String documentoReu) {
        this.documentoReu = documentoReu;
    }

    public TipoDepositante getTipoDepositante() {
        return tipoDepositante;
    }

    public void setTipoDepositante(TipoDepositante tipoDepositante) {
        this.tipoDepositante = tipoDepositante;
    }

    public String getNomeDepositante() {
        return nomeDepositante;
    }

    public void setNomeDepositante(String nomeDepositante) {
        this.nomeDepositante = nomeDepositante;
    }

    public TipoPessoa getTipoPessoaDepositante() {
        return tipoPessoaDepositante;
    }

    public void setTipoPessoaDepositante(TipoPessoa tipoPessoaDepositante) {
        this.tipoPessoaDepositante = tipoPessoaDepositante;
    }

    public String getDocumentoDepositante() {
        return documentoDepositante;
    }

    public void setDocumentoDepositante(String documentoDepositante) {
        this.documentoDepositante = documentoDepositante;
    }

    public String getNomeAdvogadoAutor() {
        return nomeAdvogadoAutor;
    }

    public void setNomeAdvogadoAutor(String nomeAdvogadoAutor) {
        this.nomeAdvogadoAutor = nomeAdvogadoAutor;
    }

    public TipoPessoa getTipoPessoaAdvogadoAutor() {
        return tipoPessoaAdvogadoAutor;
    }

    public void setTipoPessoaAdvogadoAutor(TipoPessoa tipoPessoaAdvogadoAutor) {
        this.tipoPessoaAdvogadoAutor = tipoPessoaAdvogadoAutor;
    }

    public String getDocumentoAdvogadoAutor() {
        return documentoAdvogadoAutor;
    }

    public void setDocumentoAdvogadoAutor(String documentoAdvogadoAutor) {
        this.documentoAdvogadoAutor = documentoAdvogadoAutor;
    }

    public String getNomeAdvogadoReu() {
        return nomeAdvogadoReu;
    }

    public void setNomeAdvogadoReu(String nomeAdvogadoReu) {
        this.nomeAdvogadoReu = nomeAdvogadoReu;
    }

    public TipoPessoa getTipoPessoaAdvogadoReu() {
        return tipoPessoaAdvogadoReu;
    }

    public void setTipoPessoaAdvogadoReu(TipoPessoa tipoPessoaAdvogadoReu) {
        this.tipoPessoaAdvogadoReu = tipoPessoaAdvogadoReu;
    }

    public String getDocumentoAdvogadoReu() {
        return documentoAdvogadoReu;
    }

    public void setDocumentoAdvogadoReu(String documentoAdvogadoReu) {
        this.documentoAdvogadoReu = documentoAdvogadoReu;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public Long getOrgaoId() {
        return orgaoId;
    }

    public void setOrgaoId(Long orgaoId) {
        this.orgaoId = orgaoId;
    }

    public Integer getCodComarca() {
        return codComarca;
    }

    public void setCodComarca(Integer codComarca) {
        this.codComarca = codComarca;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Long getContaDestinoId() {
        return contaDestinoId;
    }

    public void setContaDestinoId(Long contaDestinoId) {
        this.contaDestinoId = contaDestinoId;
    }

    public Requisitante getRequisitante() {
        return requisitante;
    }

    public void setRequisitante(Requisitante requisitante) {
        this.requisitante = requisitante;
    }

    public Long getNumeroGuia() {
        return numeroGuia;
    }

    public void setNumeroGuia(Long numeroGuia) {
        this.numeroGuia = numeroGuia;
    }

    public Long getIdDeposito() {
        return idDeposito;
    }

    public void setIdDeposito(Long idDeposito) {
        this.idDeposito = idDeposito;
    }
}
