package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.enums.SistemaOrigemEnum;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebidaStatus.FINALIZADA;
import static br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebidaStatus.PENDENTE;

@Entity
@Table(name = "MINUTA_RECEBIDA",schema = "MODULOGABINETE")
@JsonIgnoreProperties(ignoreUnknown = true)
@SequenceGenerator(name = MinutaRecebida.SEQUENCE_NAME, sequenceName = MinutaRecebida.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class MinutaRecebida {


    protected MinutaRecebida(){
        idProcessoSistemaLegado = null;
    }

    @JsonCreator
    public MinutaRecebida(@JsonProperty("idProcessoSistemaLegado") Long idProcessoSistemaLegado,
                          @JsonProperty("sistema") FonteDadosEnum sistema,
                          @JsonProperty("idMovimentoProcessual") Long idMovimento,
                          @JsonProperty("html") String html) throws Exception {
        if(sistema == null)
            throw new Exception("O sistema do processo deve ser informado");
        if(idProcessoSistemaLegado == null)
            throw new Exception("O Id do processo deve ser informado");
        if(idMovimento == null)
            throw new Exception("O Id do movimento deve ser informado");
        if(html == null || html.length() <= 1)
            throw new Exception("O html deve ser informado");
        this.idProcessoSistemaLegado = idProcessoSistemaLegado;
        this.sistema = sistema;
        this.html = html;
        this.idMovimentoProcessual = idMovimento;
    }

    public static final String SEQUENCE_NAME = "SEQ_MINUTA_RECEBIDA";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @NotNull
    private Long id;

    @Column(name = "SISTEMA")
    @Enumerated(EnumType.STRING)
    private FonteDadosEnum sistema;

    @Transient
    private final Long idProcessoSistemaLegado;

    @Column(name = "id_movimento")
    private Long idMovimentoProcessual;

    @Column(name = "html")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String html;

    @Column(name = "sistema_origin")
    private SistemaOrigemEnum sistemaOrigem;

    @Column(name = "ref_origem")
    private String referenciaOrigem;

    @Column(name = "nome_autor")
    private String nomeAutor = "Não Informado";

    @Column(name = "data_criacao")
    private Date dataCriacao = new Date();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private MinutaRecebidaStatus status = PENDENTE;

    @ManyToOne
    @JoinColumn(name = "id_processo", foreignKey = @ForeignKey(name = "FK_MINUTA_PROCESSO"))
    @NotNull(message = "Processo não pode ser nulo")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Processo processo;

    @JsonIgnore
    public Minuta getMinuta(){
        return getMinuta(new Minuta());
    }

    @JsonIgnore
    public Minuta getMinuta(Minuta minuta){
        if(minuta.getProcesso() == null)
            minuta.setProcesso(processo);
        minuta.setMinutaHtml(html);
        minuta.getMinutaMovimentos(); //TODO setar movimento ao transformar minuta
        //TODO talvez um dia passar esses valores para salvar com versao - minuta.defineAutoria();
        return minuta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FonteDadosEnum getSistema() {
        return sistema;
    }

    public void setSistema(FonteDadosEnum sistema) {
        this.sistema = sistema;
    }

    public Long getIdProcessoSistemaLegado() {
        return idProcessoSistemaLegado;
    }

    public Long getIdMovimentoProcessual() {
        return idMovimentoProcessual;
    }

    public void setIdMovimentoProcessual(Long idMovimentoProcessual) {
        this.idMovimentoProcessual = idMovimentoProcessual;
    }

    public String getReferenciaOrigem() {
        return referenciaOrigem;
    }

    public void setReferenciaOrigem(String referenciaOrigem) {
        this.referenciaOrigem = referenciaOrigem;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public SistemaOrigemEnum getSistemaOrigem() {
        return sistemaOrigem;
    }

    public void setSistemaOrigem(SistemaOrigemEnum sistemaOrigin) {
        this.sistemaOrigem = sistemaOrigin;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public Processo getProcesso() {
        return processo;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public MinutaRecebidaStatus getStatus() {
        return status;
    }

    public void setStatus(MinutaRecebidaStatus status) {
        this.status = status;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void finalizada() {
        status = FINALIZADA;
    }

    public void comErro() {
        setStatus(MinutaRecebidaStatus.COM_ERRO);
    }

    public MinutaResposta getResposta() {
        return new MinutaResposta(sistemaOrigem, referenciaOrigem, status);
    }

    public void setProcesso(Processo processo, Optional<ProcessoTag> tagValidarExpediente, Caixa caixaValidarExpediente) throws Exception {
        if (processo.getTarefa() == TarefaEnum.NaoConcluso) {
            processo.setCaixa(caixaValidarExpediente);
        }
        if(tagValidarExpediente.isPresent() && !tagValidarExpediente.get().getTag().isDeSistema()) {
            throw new Exception("A tag atribuida deve ser uma tag de sistema");
        }
        tagValidarExpediente.ifPresent(processoTag -> processo.getTags().add(processoTag));
        this.processo = processo;
    }
}
