package br.jus.tjro.gabinete.model.gab.processo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;


@Entity
@Table(name = "processo_movimento")  // processo_evento no pje
@SequenceGenerator(name = ProcessoMovimento.SEQUENCE_NAME, sequenceName = ProcessoMovimento.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class ProcessoMovimento {

    static final String SEQUENCE_NAME = "SEQUENCIA_PROCESSO_MOVIMENTO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "id_proc_mov_legado")
    private Long idProcessoMovimentoLegado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_processo", foreignKey = @ForeignKey(name = "FK_PROCESSO_MOVIMENTO_PROCESSO"))
    @NotNull(message = "Processo não pode ser nulo")
    @JsonInclude(Include.NON_NULL)
    private Processo processo;

    @Column(name = "cd_evento")
    @JsonInclude(Include.NON_NULL)
    private Long movimento;

    private Date data_atualizacao;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_processo_documento", foreignKey = @ForeignKey(name = "FK_PROCESSO_MOVIMENTO_PROC_DOC"))
    private ProcessoDocumento processoDocumento;

    @Column(length = 3000)
    private String descricao;

    @Column(name = "in_visibilidade_externa")
    private Boolean in_visibilidade_externa;

    @Column(name = "in_ativo")
    private Boolean in_ativo;

    private Integer id_orgao_julgador;

    private Integer id_orgao_julgador_colegiado;

    @Column(length = 100)
    private String nome_usuario;

    @Column(length = 14)
    private String cpf_usuario;

    public ProcessoMovimento() {
    }

    public ProcessoMovimento(
        Processo processo,
        Long movimento,
        ProcessoDocumento processoDocumento,
        Long idProcessoMovimentoLegado, Date data_atualizacao, String descricao,
        Boolean in_visibilidade_externa, Boolean in_ativo, Integer id_orgao_julgador,
        Integer id_orgao_julgador_colegiado, String nome_usuario, String cpf_usuario
    ) {
        this.idProcessoMovimentoLegado = idProcessoMovimentoLegado;
        this.processo = processo;
        this.movimento = movimento;
        this.data_atualizacao = data_atualizacao;
        this.processoDocumento = processoDocumento;
        this.descricao = descricao;
        this.in_visibilidade_externa = in_visibilidade_externa;
        this.in_ativo = in_ativo;
        this.id_orgao_julgador = id_orgao_julgador;
        this.id_orgao_julgador_colegiado = id_orgao_julgador_colegiado;
        this.nome_usuario = nome_usuario;
        this.cpf_usuario = cpf_usuario;
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

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public Long getMovimento() {
        return movimento;
    }

    public void setMovimento(Long movimento) {
        this.movimento = movimento;
    }

    public Date getData_atualizacao() {
        return data_atualizacao;
    }

    public void setData_atualizacao(Date data_atualizacao) {
        this.data_atualizacao = data_atualizacao;
    }

    public ProcessoDocumento getProcessoDocumento() {
        return processoDocumento;
    }

    public void setProcessoDocumento(ProcessoDocumento processoDocumento) {
        this.processoDocumento = processoDocumento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getIn_visibilidade_externa() {
        return in_visibilidade_externa;
    }

    public void setIn_visibilidade_externa(Boolean in_visibilidade_externa) {
        this.in_visibilidade_externa = in_visibilidade_externa;
    }

    public Boolean getIn_ativo() {
        return in_ativo;
    }

    public void setIn_ativo(Boolean in_ativo) {
        this.in_ativo = in_ativo;
    }

    public Integer getId_orgao_julgador() {
        return id_orgao_julgador;
    }

    public void setId_orgao_julgador(Integer id_orgao_julgador) {
        this.id_orgao_julgador = id_orgao_julgador;
    }

    public Integer getId_orgao_julgador_colegiado() {
        return id_orgao_julgador_colegiado;
    }

    public void setId_orgao_julgador_colegiado(Integer id_orgao_julgador_colegiado) {
        this.id_orgao_julgador_colegiado = id_orgao_julgador_colegiado;
    }

    public String getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    public String getCpf_usuario() {
        return cpf_usuario;
    }

    public void setCpf_usuario(String cpf_usuario) {
        this.cpf_usuario = cpf_usuario;
    }
}
