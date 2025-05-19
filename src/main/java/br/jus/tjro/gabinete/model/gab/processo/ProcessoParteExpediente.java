package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.interfaces.importacao.EntidadeDoisBancos;
import br.jus.tjro.gabinete.model.gab.enums.TipoCalculoMeioComunicacaoEnum;
import br.jus.tjro.gabinete.model.gab.enums.TipoPrazoEnum;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoParteExpedienteTransiente;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static br.jus.tjro.gabinete.util.StringUtils.getString;

@Entity
@Table(name = "PROCESSO_PARTE_EXPEDIENTE")
@SequenceGenerator(name = ProcessoParteExpediente.SEQUENCE_NAME, sequenceName = ProcessoParteExpediente.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class ProcessoParteExpediente implements EntidadeDoisBancos {

    static final String SEQUENCE_NAME = "SEQ_PROCESSO_PARTE_EXPEDIENTE";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_processo_expediente", foreignKey = @ForeignKey(name = "FK_PROC_PARTE_EXP_PROC_EXP"))
    @NotNull(message = "Expediente não pode ser nulo")
    private ProcessoExpediente processoExpediente;

    @Column(name = "id_legado")
    @NotNull(message = "id_legado não pode ser nulo")
    private Long idLegado;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa_parte", nullable = false, foreignKey = @ForeignKey(name = "FK_PROC_PARTE_EXP_PESS_PARTE"))
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Pessoa pessoaParte;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa_ciencia", foreignKey = @ForeignKey(name = "FK_PROC_PARTE_EXP_PESS_CIENCIA"))
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Pessoa pessoaCiencia;

    @Column(name = "qt_prazo_legal_parte")
    private Integer prazoLegal;

    @Column(name = "qt_prazo_processual_parte")
    private Integer prazoProcessual;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_prazo_legal_parte")
    private Date dtPrazoLegal;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_prazo_processual_parte")
    private Date dtPrazoProcessual;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_ciencia_parte")
    private Date dtCienciaParte;

    @Column(name = "ciencia_sistema")
    private Boolean cienciaSistema;

    @Column(name = "pendente_manifestacao")
    @NotNull
    private Boolean pendenteManifestacao = Boolean.FALSE;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "tipo_prazo", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoPrazoEnum tipoPrazo = TipoPrazoEnum.D;

    @Column(name = "pendencia", length = 200)
    @Length(max = 200)
    private String pendencia;

    @Column(name = "fechado",  nullable = false)
    @NotNull
    private Boolean fechado = Boolean.FALSE;

    @Column(name = "recibo_dje")
    private String reciboPublicacaoDiarioEletronico;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_procuradoria", foreignKey = @ForeignKey(name = "FK_PROC_PARTE_EXP_PROCURADORIA"))
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Procuradoria procuradoria;

    @Column(name = "tipo_calc_meio_comunicacao", length = 3)
    @Enumerated(EnumType.STRING)
    private TipoCalculoMeioComunicacaoEnum tipoCalculoMeioComunicacao;

    @Column(name = "intima_pessoal", nullable = false)
    @NotNull
    private Boolean intimacaoPessoal = Boolean.FALSE;

    @Column(name = "destaque", nullable = false)
    private Boolean destaque = Boolean.TRUE;

    public void setPropriedadesRemotas(ProcessoParteExpedienteTransiente ppe, Pessoa pessoaParte, Pessoa pessoaCiencia, Procuradoria procuradoria, ProcessoExpediente processoExpediente) {
        this.pessoaParte = pessoaParte;
        this.pessoaCiencia = pessoaCiencia;
        this.procuradoria = procuradoria;
        this.processoExpediente = processoExpediente;
        this.idLegado = ppe.getIdLegado();
        this.cienciaSistema = ppe.getCienciaSistema();
        this.destaque = ppe.getDestaque();
        this.dtCienciaParte = ppe.getDataCienciaParte();
        this.dtPrazoLegal = ppe.getDtPrazoLegal();
        this.dtPrazoProcessual = ppe.getDtPrazoProcessual();
        this.fechado = ppe.getFechado();
        this.intimacaoPessoal = ppe.getIntimaPessoal();
        this.pendencia = ppe.getPendencia();
        this.prazoLegal = (ppe.getPrazoLegal() != null ) ? ppe.getPrazoLegal().intValue() : null;
        this.pendenteManifestacao = ppe.getPendenteManifestacao();
        this.prazoProcessual = (ppe.getPrazoProcessual() != null) ? ppe.getPrazoProcessual().intValue() : null;
        this.reciboPublicacaoDiarioEletronico = ppe.getReciboDje();
        this.tipoCalculoMeioComunicacao = (ppe.getTipoCalculoMeioComunicacao() != null && !ppe.getTipoCalculoMeioComunicacao().equals("")) ? TipoCalculoMeioComunicacaoEnum.valueOf(ppe.getTipoCalculoMeioComunicacao()) : null;
        this.tipoPrazo = ppe.getTipoPrazo();
    }

    @Override
    @JsonIgnore
    public String getObjetoKeyString() {
        return idLegado.toString();
    }

    @Override
    @JsonIgnore
    public String getObjetoUpdateString() {
        List<String> campos = List.of(
            getString((pessoaParte != null) ? pessoaParte.getIdPessoaLegado() : null),
            getString(pessoaCiencia != null ? pessoaCiencia.getIdPessoaLegado() : null),
            getString(processoExpediente != null ? processoExpediente.getIdLegado() : null),
            getString(dtCienciaParte),
            getString(prazoLegal),
            getString(dtPrazoLegal),
            getString(prazoProcessual),
            getString(cienciaSistema),
            getString(pendenteManifestacao),
            getString(pendencia),
            getString(fechado),
            getString(tipoPrazo),
            getString(reciboPublicacaoDiarioEletronico),
            getString(intimacaoPessoal),
            getString(tipoCalculoMeioComunicacao),
            getString(destaque));
        return String.join("", campos);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessoExpediente getProcessoExpediente() {
        return processoExpediente;
    }

    public void setProcessoExpediente(ProcessoExpediente processoExpediente) {
        this.processoExpediente = processoExpediente;
    }

    public Long getIdLegado() {
        return idLegado;
    }

    public void setIdLegado(Long idLegado) {
        this.idLegado = idLegado;
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

    public Integer getPrazoLegal() {
        return prazoLegal;
    }

    public void setPrazoLegal(Integer prazoLegal) {
        this.prazoLegal = prazoLegal;
    }

    public Integer getPrazoProcessual() {
        return prazoProcessual;
    }

    public void setPrazoProcessual(Integer prazoProcessual) {
        this.prazoProcessual = prazoProcessual;
    }

    public Date getDtPrazoLegal() {
        return dtPrazoLegal;
    }

    public void setDtPrazoLegal(Date dtPrazoLegal) {
        this.dtPrazoLegal = dtPrazoLegal;
    }

    public Date getDtPrazoProcessual() {
        return dtPrazoProcessual;
    }

    public void setDtPrazoProcessual(Date dtPrazoProcessual) {
        this.dtPrazoProcessual = dtPrazoProcessual;
    }

    public Date getDtCienciaParte() {
        return dtCienciaParte;
    }

    public void setDtCienciaParte(Date dtCienciaParte) {
        this.dtCienciaParte = dtCienciaParte;
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

    public TipoPrazoEnum getTipoPrazo() {
        return tipoPrazo;
    }

    public void setTipoPrazo(TipoPrazoEnum tipoPrazo) {
        this.tipoPrazo = tipoPrazo;
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

    public String getReciboPublicacaoDiarioEletronico() {
        return reciboPublicacaoDiarioEletronico;
    }

    public void setReciboPublicacaoDiarioEletronico(String reciboPublicacaoDiarioEletronico) {
        this.reciboPublicacaoDiarioEletronico = reciboPublicacaoDiarioEletronico;
    }

    public Procuradoria getProcuradoria() {
        return procuradoria;
    }

    public void setProcuradoria(Procuradoria procuradoria) {
        this.procuradoria = procuradoria;
    }

    public TipoCalculoMeioComunicacaoEnum getTipoCalculoMeioComunicacao() {
        return tipoCalculoMeioComunicacao;
    }

    public void setTipoCalculoMeioComunicacao(TipoCalculoMeioComunicacaoEnum tipoCalculoMeioComunicacao) {
        this.tipoCalculoMeioComunicacao = tipoCalculoMeioComunicacao;
    }

    public Boolean getIntimacaoPessoal() {
        return intimacaoPessoal;
    }

    public void setIntimacaoPessoal(Boolean intimacaoPessoal) {
        this.intimacaoPessoal = intimacaoPessoal;
    }

    public Boolean getDestaque() {
        return destaque;
    }

    public void setDestaque(Boolean destaque) {
        this.destaque = destaque;
    }
}
