package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.model.gab.enums.ExpedicaoExpedienteEnum;
import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PROCESSO_EXPEDIENTE")
@SequenceGenerator(name = ProcessoExpediente.SEQUENCE_NAME, sequenceName = ProcessoExpediente.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class ProcessoExpediente {

    static final String SEQUENCE_NAME = "SEQ_PROCESSO_EXPEDIENTE";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = SEQUENCE_NAME)
    private Long id;


    @Column(name = "id_legado")
    @NotNull
    private Long idLegado;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "processoExpediente")
    private List<ProcessoParteExpediente> processoParteExpedientes;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_processo", foreignKey = @ForeignKey(name = "FK_PROCESSO_EXP_PROCESSO"))
    @NotNull(message = "Processo é um campo obrigatório.")
    private Processo processo;

    @Column(name = "dt_criacao_expediente")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date dtCriacao;

    @Column(name = "dt_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtExclusao;

    @Column(name = "meio_expediente")
    @Enumerated(EnumType.STRING)
    private ExpedicaoExpedienteEnum meioExpedicaoExpediente = ExpedicaoExpedienteEnum.E;

    @Transient
    private String meioExpedicao;

    @Column(name = "urgencia")
    private Boolean urgencia = Boolean.FALSE;

    @Column(name = "documento_existente", nullable = false)
    private Boolean documentoExistente = Boolean.FALSE;

    @OneToOne
    @JoinColumn(name = "id_processo_doc_vinculado", foreignKey = @ForeignKey(name = "FK_PROCESSO_EXP_DOC_VINCULADO"))
    private ProcessoDocumento processoDocumentoVinculadoExpediente;

    @ManyToOne
    @JoinColumn(name = "id_processo_documento", foreignKey = @ForeignKey(name = "FK_PROC_EXP_PROCESSO_DOCUMENTO"))
    private ProcessoDocumento processoDocumento;


    @Column(name = "temporario", nullable = false)
    private Boolean inTemporario;

    public String getMeioExpedicao() {
        return (this.meioExpedicaoExpediente != null) ? this.meioExpedicaoExpediente.getLabel() : "";
    }

    public void setPropriedadesRemota(Processo processo, ProcessoExpediente remoto, ProcessoDocumento processoDocumento, ProcessoDocumento processoDocumentoVinculado) {
        this.processo = processo;
        this.idLegado = remoto.getIdLegado();
        this.processoDocumento = processoDocumento;
        this.processoDocumentoVinculadoExpediente = processoDocumentoVinculado;
        this.documentoExistente = remoto.getDocumentoExistente();
        this.dtCriacao = remoto.getDtCriacao();
        this.dtExclusao = remoto.getDtExclusao();
        this.inTemporario = remoto.getInTemporario();
        this.meioExpedicaoExpediente = remoto.getMeioExpedicaoExpediente();
        this.urgencia = remoto.getUrgencia();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public List<ProcessoParteExpediente> getProcessoParteExpedientes() {
        return processoParteExpedientes;
    }

    public void setProcessoParteExpedientes(List<ProcessoParteExpediente> processoParteExpedientes) {
        this.processoParteExpedientes = processoParteExpedientes;
    }

    public Long getIdLegado() {
        return idLegado;
    }

    public void setIdLegado(Long idLegado) {
        this.idLegado = idLegado;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtExclusao() {
        return dtExclusao;
    }

    public void setDtExclusao(Date dtExclusao) {
        this.dtExclusao = dtExclusao;
    }

    public ExpedicaoExpedienteEnum getMeioExpedicaoExpediente() {
        return meioExpedicaoExpediente;
    }

    public void setMeioExpedicaoExpediente(ExpedicaoExpedienteEnum meioExpedicaoExpediente) {
        this.meioExpedicaoExpediente = meioExpedicaoExpediente;
    }

    public Boolean getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(Boolean urgencia) {
        this.urgencia = urgencia;
    }

    public Boolean getDocumentoExistente() {
        return documentoExistente;
    }

    public void setDocumentoExistente(Boolean documentoExistente) {
        this.documentoExistente = documentoExistente;
    }

    public Boolean getInTemporario() {
        return inTemporario;
    }

    public void setMeioExpedicao(String meioExpedicao) {
        this.meioExpedicao = meioExpedicao;
    }

    public ProcessoDocumento getProcessoDocumentoVinculadoExpediente() {
        return processoDocumentoVinculadoExpediente;
    }

    public void setProcessoDocumentoVinculadoExpediente(ProcessoDocumento processoDocumentoVinculadoExpediente) {
        this.processoDocumentoVinculadoExpediente = processoDocumentoVinculadoExpediente;
    }

    public ProcessoDocumento getProcessoDocumento() {
        return processoDocumento;
    }

    public void setProcessoDocumento(ProcessoDocumento processoDocumento) {
        this.processoDocumento = processoDocumento;
    }

    public void setInTemporario(Boolean inTemporario) {
        this.inTemporario = inTemporario;
    }
}
