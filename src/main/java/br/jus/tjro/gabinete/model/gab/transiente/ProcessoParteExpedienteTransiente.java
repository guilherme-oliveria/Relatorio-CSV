package br.jus.tjro.gabinete.model.gab.transiente;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.EntidadeDoisBancos;
import br.jus.tjro.gabinete.model.gab.enums.TipoCalculoMeioComunicacaoEnum;
import br.jus.tjro.gabinete.model.gab.enums.TipoPrazoEnum;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoExpediente;
import br.jus.tjro.gabinete.model.gab.processo.Procuradoria;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static br.jus.tjro.gabinete.util.StringUtils.getString;

public class ProcessoParteExpedienteTransiente implements EntidadeDoisBancos {
    private Pessoa pessoaParte;
    private Pessoa pessoaCiencia;
    private Procuradoria procuradoria;
    private ProcessoExpediente processoExpediente;
    private ProcessoDocumento processoExpDocumento;
    private ProcessoDocumento processoExpDocumentoVinculado;

    private Long idLegado;
    private Long idLegadoPessoaParte;
    private Long idLegadoPessoaCiencia;
    private Long idLegadoProcuradoria;
    private Long idLegadoProcessoExpediente;
    private Date dataCienciaParte;
    private Long prazoLegal;
    private Date dtPrazoLegal;
    private Long prazoProcessual;
    private Date dtPrazoProcessual;
    private Boolean cienciaSistema;
    private Boolean pendenteManifestacao;
    private String pendencia;
    private Boolean fechado;
    private TipoPrazoEnum tipoPrazo;
    private String reciboDje;
    private Boolean intimaPessoal;
    private String tipoCalculoMeioComunicacao;
    private Boolean destaque;
    private Boolean enviadoKafka;
    private Long idProcessoSistemalegado;
    private FonteDadosEnum sistema;

    @Override
    public String getObjetoKeyString() {
        return idLegado.toString();
    }

    @Override
    public String getObjetoUpdateString() {
        List<String> campos = List.of(
            getString(idLegadoPessoaParte),
            getString(idLegadoPessoaCiencia),
            getString(idLegadoProcessoExpediente),
            getString(dataCienciaParte),
            getString(prazoLegal),
            getString(dtPrazoLegal),
            getString(prazoProcessual),
            getString(cienciaSistema),
            getString(pendenteManifestacao),
            getString(pendencia),
            getString(fechado),
            getString(tipoPrazo),
            getString(reciboDje),
            getString(intimaPessoal),
            getString(tipoCalculoMeioComunicacao),
            getString(destaque));
        return String.join("", campos);
    }

    public Pessoa getPessoaParte() {
        return pessoaParte;
    }

    public void setPessoaParte(Pessoa pessoaParte) {
        this.pessoaParte = pessoaParte;
    }

    public Pessoa getPessoaCiencia() {
        return pessoaCiencia;
    }

    public void setPessoaCiencia(Pessoa pessoaCiencia) {
        this.pessoaCiencia = pessoaCiencia;
    }

    public Procuradoria getProcuradoria() {
        return procuradoria;
    }

    public void setProcuradoria(Procuradoria procuradoria) {
        this.procuradoria = procuradoria;
    }

    public ProcessoExpediente getProcessoExpediente() {
        return processoExpediente;
    }

    public void setProcessoExpediente(ProcessoExpediente processoExpediente) {
        this.processoExpediente = processoExpediente;
    }

    public ProcessoDocumento getProcessoExpDocumento() {
        return processoExpDocumento;
    }

    public void setProcessoExpDocumento(ProcessoDocumento processoExpDocumento) {
        this.processoExpDocumento = processoExpDocumento;
    }

    public ProcessoDocumento getProcessoExpDocumentoVinculado() {
        return processoExpDocumentoVinculado;
    }

    public void setProcessoExpDocumentoVinculado(ProcessoDocumento processoExpDocumentoVinculado) {
        this.processoExpDocumentoVinculado = processoExpDocumentoVinculado;
    }

    public Long getIdLegado() {
        return idLegado;
    }

    public void setIdLegado(Long idLegado) {
        this.idLegado = idLegado;
    }

    public Long getIdLegadoPessoaParte() {
        return idLegadoPessoaParte;
    }

    public void setIdLegadoPessoaParte(Long idLegadoPessoaParte) {
        this.idLegadoPessoaParte = idLegadoPessoaParte;
    }

    public Long getIdLegadoPessoaCiencia() {
        return idLegadoPessoaCiencia;
    }

    public void setIdLegadoPessoaCiencia(Long idLegadoPessoaCiencia) {
        this.idLegadoPessoaCiencia = idLegadoPessoaCiencia;
    }

    public Long getIdLegadoProcuradoria() {
        return idLegadoProcuradoria;
    }

    public void setIdLegadoProcuradoria(Long idLegadoProcuradoria) {
        this.idLegadoProcuradoria = idLegadoProcuradoria;
    }

    public Long getIdLegadoProcessoExpediente() {
        return idLegadoProcessoExpediente;
    }

    public void setIdLegadoProcessoExpediente(Long idLegadoProcessoExpediente) {
        this.idLegadoProcessoExpediente = idLegadoProcessoExpediente;
    }

    public Date getDataCienciaParte() {
        return dataCienciaParte;
    }

    public void setDataCienciaParte(Date dataCienciaParte) {
        this.dataCienciaParte = dataCienciaParte;
    }

    public Long getPrazoLegal() {
        return prazoLegal;
    }

    public void setPrazoLegal(Long prazoLegal) {
        this.prazoLegal = prazoLegal;
    }

    public Date getDtPrazoLegal() {
        return dtPrazoLegal;
    }

    public void setDtPrazoLegal(Date dtPrazoLegal) {
        this.dtPrazoLegal = dtPrazoLegal;
    }

    public Long getPrazoProcessual() {
        return prazoProcessual;
    }

    public void setPrazoProcessual(Long prazoProcessual) {
        this.prazoProcessual = prazoProcessual;
    }

    public Date getDtPrazoProcessual() {
        return dtPrazoProcessual;
    }

    public void setDtPrazoProcessual(Date dtPrazoProcessual) {
        this.dtPrazoProcessual = dtPrazoProcessual;
    }

    public Boolean getCienciaSistema() {
        return cienciaSistema;
    }

    public void setCienciaSistema(Boolean cienciaSistema) {
        this.cienciaSistema = cienciaSistema;
    }

    public Boolean getPendenteManifestacao() {
        return pendenteManifestacao;
    }

    public void setPendenteManifestacao(Boolean pendenteManifestacao) {
        this.pendenteManifestacao = pendenteManifestacao;
    }

    public String getPendencia() {
        return pendencia;
    }

    public void setPendencia(String pendencia) {
        this.pendencia = pendencia;
    }

    public Boolean getFechado() {
        return fechado;
    }

    public void setFechado(Boolean fechado) {
        this.fechado = fechado;
    }

    public TipoPrazoEnum getTipoPrazo() {
        return tipoPrazo;
    }

    public void setTipoPrazo(TipoPrazoEnum tipoPrazo) {
        this.tipoPrazo = tipoPrazo;
    }

    public String getReciboDje() {
        return reciboDje;
    }

    public void setReciboDje(String reciboDje) {
        this.reciboDje = reciboDje;
    }

    public Boolean getIntimaPessoal() {
        return intimaPessoal;
    }

    public void setIntimaPessoal(Boolean intimaPessoal) {
        this.intimaPessoal = intimaPessoal;
    }

    public String getTipoCalculoMeioComunicacao() {
        return tipoCalculoMeioComunicacao;
    }

    public void setTipoCalculoMeioComunicacao(String tipoCalculoMeioComunicacao) {
        this.tipoCalculoMeioComunicacao = tipoCalculoMeioComunicacao;
    }

    public Boolean getDestaque() {
        return destaque;
    }

    public void setDestaque(Boolean destaque) {
        this.destaque = destaque;
    }

    public Boolean getEnviadoKafka() {
        return enviadoKafka;
    }

    public void setEnviadoKafka(Boolean enviadoKafka) {
        this.enviadoKafka = enviadoKafka;
    }

    public Long getIdProcessoSistemalegado() {
        return idProcessoSistemalegado;
    }

    public FonteDadosEnum getSistema() {
        return sistema;
    }

    public void setProcesso(Processo processo) {
        this.idProcessoSistemalegado = processo.getIdProcessoSistemaLegado();
        this.sistema = processo.getSistema();
    }
}
