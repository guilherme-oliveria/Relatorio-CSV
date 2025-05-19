package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.infrastructure.converter.TpuAssuntoConverter;
import br.jus.tjro.gabinete.infrastructure.converter.TpuClasseConverter;
import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;


@Entity
@Table(name = "processo_assunto")
@SequenceGenerator(name = ProcessoAssunto.SEQUENCE_NAME, sequenceName = ProcessoAssunto.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class ProcessoAssunto {

    static final String SEQUENCE_NAME = "SEQUENCIA_PROCESSO_ASSUNTO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_processo", foreignKey = @ForeignKey(name = "FK_PROCESSO_ASSUNTO_PROCESSO"))
    @NotNull(message = "Processo é um campo obrigatório.")
    @JsonInclude(Include.NON_NULL)
    @JsonBackReference
    private Processo processo;

    @Column(name = "id_assunto")
    private Long idAssunto;

    @Column(name = "is_assunto_principal")
    private Boolean isAssuntoPrincipal;

    // data da inclusão do assunto no processo pelo módulo Gabinete
    @Column(name = "data_inclusao")
    private Timestamp dataInclusao;

    // se excluído pelo módulo Gabinete
    @Column(name = "is_excluido")
    private Boolean isExcluido;

    // data da exclusão do assunto no processo pelo módulo Gabinete
    @Column(name = "data_exclusao")
    private Timestamp dataExclusao;

    @Transient
    private String descricao;

    @Column(name = "id_assunto", insertable = false, updatable = false)
    @Convert(converter = TpuAssuntoConverter.class)
    private TpuAssunto assuntoTpu;

    public ProcessoAssunto() {
    }

    public ProcessoAssunto(
        Processo processo, Boolean isAssuntoPrincipal
    ) {
        this.processo = processo;
        this.isAssuntoPrincipal = isAssuntoPrincipal;

    }

    public ProcessoAssunto(Processo processo, Long idAssunto, Boolean isAssuntoPrincipal) {
        this.processo = processo;
        this.idAssunto = idAssunto;
        this.isAssuntoPrincipal = isAssuntoPrincipal;
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

    public Boolean getAssuntoPrincipal() {
        return isAssuntoPrincipal;
    }

    public void setAssuntoPrincipal(Boolean assuntoPrincipal) {
        isAssuntoPrincipal = assuntoPrincipal;
    }

    public Timestamp getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Timestamp dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public Boolean getExcluido() {
        return isExcluido;
    }

    public void setExcluido(Boolean excluido) {
        isExcluido = excluido;
    }

    public Timestamp getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(Timestamp dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public Long getIdAssunto() {
        return idAssunto;
    }

    public void setIdAssunto(Long idAssunto) {
        this.idAssunto = idAssunto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @JsonIgnore
    public TpuAssunto getAssuntoTpu() {
        return assuntoTpu;
    }

    public void setAssuntoTpu(TpuAssunto assuntoTpu) {
        this.assuntoTpu = assuntoTpu;
    }
}
