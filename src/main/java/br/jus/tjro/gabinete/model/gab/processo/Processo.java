package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.infrastructure.converter.OrgaoJulgadorConverter;
import br.jus.tjro.gabinete.infrastructure.converter.TpuClasseConverter;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebida;
import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.model.gab.transiente.Parte;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.util.LocalDateTimeConverter;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

import static br.jus.tjro.gabinete.Tarefas.core.TarefaEnum.*;
import static br.jus.tjro.gabinete.scheduled.devolve.origem.ErrosDevolveOrigem.*;
import static io.vavr.collection.Stream.*;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "processo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_processo_sistema_legado", "sistema"})})
@SequenceGenerator(name = Processo.SEQUENCE_NAME, sequenceName = Processo.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Processo implements Serializable {

    public static final String SEQUENCE_NAME = "SEQUENCIA_PROCESSO";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Long id;

    @Column(name = "id_processo_sistema_legado")
    private Long idProcessoSistemaLegado;

    @Column(name = "numero_processo", length = 25)
    private String numeroProcesso;

    @Column(name = "orgao_julgador_str")
    @Convert(converter = OrgaoJulgadorConverter.class)
    private OrgaoJulgador orgaoJulgadorObj;

    @Transient
    private TpuClasse tpuClasse;

    @Column(name = "classe_judicial")
    private Long idClasseJudicial;

    @Column(name = "DATA_ENTRADA")
    @Convert(converter = LocalDateTimeConverter.class)
    @JsonIgnore
    private LocalDateTime dataEntrada;

    @Column(name = "DATA_SINCRONIZACAO")
    private Date dataSincronizacao = new Date();

    @Column(name = "ERRO_SINCRONIZACAO")
    private Integer erroSincronizacao = 0;

    @Column(name = "POSSUI_LIMINAR")
    private boolean possuiLiminar;

    @Column(name = "PRIORIDADE")
    private boolean prioridade;

    @Column(name = "SEGREDO_JUSTICA")
    private Boolean segredoJustica;

    @Column(name = "DIGITAL")
    private Boolean digital;

    @Column(name = "MOTIVO_SEGREDO_JUSTICA")
    private String motivoSegredoJustica;

    @Column(name = "JUSTICA_GRATUITA")
    private boolean justicaGratuita;

    @Column(name = "SISTEMA")
    @Enumerated(EnumType.STRING)
    private FonteDadosEnum sistema;

    @Enumerated(EnumType.STRING)
    @Column(name = "tarefaenum")
    private TarefaEnum tarefaEnum;

    @Transient
    @JsonIgnore
    private TarefaEnum tarefaEnumAnterior = Minutar;

    @Transient
    private String manifestacaoDescricao;

    @Column(name = "dt_distribuicao")
    private Date dataUltimaDistribuicao;

    @Column(name = "vl_causa")
    private Double valorCausa;

    @Column(name = "ASSUNTO_PRINCIPAL", length = 100)
    private String assuntoPrincipal;

    @Column(name = "competencia", length = 255)
    private String competencia;

    @Formula(value = "CURRENT_DATE - CAST(DATA_ENTRADA AS DATE)")
    private Integer tempoConcluso;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "processo_prioridade",
        joinColumns = @JoinColumn(
            name = "PROCESSO_ID", foreignKey = @ForeignKey(name = "FK_PROCESSO_PRIORIDADE_PROC")),
        inverseJoinColumns = @JoinColumn(name = "PRIORIDADES_ID", foreignKey = @ForeignKey(name = "FK_PROCESSO_PRIORIDADE_PRI_PRO"))
    )
    private Set<PrioridadeProcessual> prioridades;

    @Formula("(select coalesce(sum(pp.prioridades_id),0) from processo_prioridade pp where pp.processo_id = id)")
    private Integer totalPrioridade;

    @ManyToOne
    @JoinColumn(name = "id_caixa", foreignKey = @ForeignKey(name = "FK_PROCESSO_CAIXA"))
    private Caixa caixa;

    @OneToMany(mappedBy = "processo", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Minuta> minutas = new HashSet<>();

    @OneToMany(mappedBy = "processo")
    @JsonIgnore
    private Set<MinutaRecebida> minutasRecebidas = new HashSet<>();

    @OneToMany(mappedBy = "processo", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ProcessoTag> tags;

    @OneToMany(mappedBy = "processo", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ProcessoAssunto> processoAssuntos;

    @OneToMany(mappedBy = "processo")
    @JsonManagedReference
    @JsonIgnore
    private List<ProcessoMovimento> processoMovimentos;

    @Formula("(select max(pm.descricao) from processo_movimento pm where pm.id_processo = id)")
    private String ultimoMovimento;

    @JsonIgnore
    @OneToMany(mappedBy = "processo", fetch = FetchType.EAGER)
    private List<ProcessoExpediente> processoExpedientes;

    @OneToMany(mappedBy = "processo")
    @JsonIgnore
    private List<ProcessoParte> processoPartes;

    @Column(name = "nivel_sigilo")
    private Integer nivelSigilo;

    @Column(name = "id_colegiado")
    private String idColegiado;
/*
    @Column(name = "erro_integracao")
    private String erroIntegracao;*/

    @Transient
    private String dataEntradaFormatada;

    @Transient
    private List<Parte> partes;

    public Processo() {
    }

    public Processo(Long id) {
        super();
        this.id = id;
    }

    public Processo(Long id, FonteDadosEnum sistema, Long idProcessoSistemaLegado) {
        super();
        this.id = id;
        this.sistema = sistema;
        this.idProcessoSistemaLegado = idProcessoSistemaLegado;
    }

    public List<ProcessoParte> getProcessoPartes() {
        return processoPartes;
    }

    public void setProcessoPartes(List<ProcessoParte> processoPartes) {
        this.processoPartes = processoPartes;
    }


    @PostLoad
    private void postLoad() {
        this.formataData();
        this.setaTarefaEnumAnterior();
    }


    private void formataData() {
        DateTimeFormatter formatador = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(new Locale("pt", "br"));
        if (dataEntrada != null)
            this.dataEntradaFormatada = this.dataEntrada.format(formatador);
    }

    public FonteDadosEnum getSistema() {
        return sistema;
    }

    public void setSistema(FonteDadosEnum sistema) {
        this.sistema = sistema;
    }

    public FonteDadosEnum getFonteDados() {
        return sistema;
    }

    public void setFonteDados(FonteDadosEnum sistema) {
        this.sistema = sistema;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public OrgaoJulgador getOrgaoJulgadorObj() {
        return orgaoJulgadorObj;
    }

    public void setOrgaoJulgadorObj(OrgaoJulgador orgaoJulgadorObj) {
        this.orgaoJulgadorObj = orgaoJulgadorObj;
    }

    public boolean isPossuiLiminar() {
        return possuiLiminar;
    }

    public void setPossuiLiminar(boolean possuiLiminar) {
        this.possuiLiminar = possuiLiminar;
    }

    public boolean isPrioridade() {
        return prioridade;
    }

    public void setPrioridade(boolean prioridade) {
        this.prioridade = prioridade;
    }

    public Boolean isSegredoJustica() {
        return segredoJustica != null && segredoJustica;
    }

    public void setSegredoJustica(Boolean segredoJustica) {
        this.segredoJustica = segredoJustica;
    }

    public Boolean isJusticaGratuita() {
        return justicaGratuita;
    }

    public void setJusticaGratuita(boolean justicaGratuita) {
        this.justicaGratuita = justicaGratuita;
    }

    public Long getIdProcessoSistemaLegado() {
        return idProcessoSistemaLegado;
    }

    public void setIdProcessoSistemaLegado(Long idProcessoSistemaLegado) {
        this.idProcessoSistemaLegado = idProcessoSistemaLegado;
    }

    public String getAssuntoPrincipal() {
        return assuntoPrincipal;
    }

    public void setAssuntoPrincipal(String assuntoPrincipal) {
        this.assuntoPrincipal = assuntoPrincipal;
    }

    public Set<PrioridadeProcessual> getPrioridades() {
        return prioridades;
    }

    public void setPrioridades(Set<PrioridadeProcessual> prioridades) {
        if(prioridades != null && prioridades.size() > 0)
            prioridade = true;
        this.prioridades = prioridades;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getDataEntradaFormatada() {
        return dataEntradaFormatada;
    }

    public void setDataEntradaFormatada(String dataEntradaFormatada) {
        this.dataEntradaFormatada = dataEntradaFormatada;
    }

    public String getUltimoMovimento() {
        return ultimoMovimento;
    }

    public void setUltimoMovimento(String ultimoMovimento) {
        this.ultimoMovimento = ultimoMovimento;
    }

    public TarefaEnum getTarefa() {
        return tarefaEnum;
    }

    /*public String getErroIntegracao() {
        return erroIntegracao;
    }

    public void setErroIntegracao(String erroIntegracao) {
        this.erroIntegracao = erroIntegracao;
    }*/

    @Deprecated
    public void setTarefa(TarefaEnum tarefa) throws Exception {
        validaTarefa(tarefa);
        if (tarefa == NaoConcluso)
            naoConcluso();
        else if(tarefa == ParaIntegracao)
            paraIntegracao();
        else
            this.tarefaEnum = tarefa;
    }

    public void validaTarefa(TarefaEnum tarefa) {
        if (TarefaEnum.getIntermediariasENaoConclusas().contains(this.tarefaEnum) && ParaIntegracaoSemManifestacao.equals(tarefa))
            throw new IllegalStateException("Uma tarefa não deve ser devolvida sem manifestação quando está com tarefa NaoConcluso");
    }

    public TarefaEnum getTarefaEnumAnterior() {
        return tarefaEnumAnterior;
    }

    private void setaTarefaEnumAnterior() {
        this.tarefaEnumAnterior = this.tarefaEnum;
    }

    public TpuClasse getTpuClasse() {
        if( this.tpuClasse == null ) {
            this.tpuClasse = new TpuClasse();
        }
        this.tpuClasse.setCodigo(idClasseJudicial);
        return this.tpuClasse;
    }

    public void setTpuClasse(TpuClasse tpuClasse) {
        if( tpuClasse == null ){
            this.tpuClasse = new TpuClasse();
            this.tpuClasse.setCodigo(idClasseJudicial);
        }
        else{
            this.tpuClasse = tpuClasse;
            if(tpuClasse.getCodigo() != null ) {
                setIdClasseJudicial(tpuClasse.getCodigo());
            }
        }
    }

    public Date getDataUltimaDistribuicao() {
        return dataUltimaDistribuicao;
    }

    public void setDataUltimaDistribuicao(Date dataUltimaDistribuicao) {
        this.dataUltimaDistribuicao = dataUltimaDistribuicao;
    }

    public Double getValorCausa() {
        return valorCausa;
    }

    public void setValorCausa(Double valorCausa) {
        this.valorCausa = valorCausa;
    }

    public Set<Minuta> getMinutas() {
        return minutas;
    }

    public void setMinutas(Set<Minuta> minutas) {
        this.minutas = minutas;
    }

    public List<ProcessoAssunto> getProcessoAssuntos() {
        return processoAssuntos;
    }

    public void setProcessoAssuntos(List<ProcessoAssunto> processoAssuntos) {
        this.processoAssuntos = processoAssuntos;
    }

    public List<ProcessoMovimento> getProcessoMovimentos() {
        return processoMovimentos;
    }

    public void setProcessoMovimentos(List<ProcessoMovimento> processoMovimentos) {
        this.processoMovimentos = processoMovimentos;
    }

    public Date getDataSincronizacao() {
        return dataSincronizacao;
    }

    public Integer getTotalPrioridade() {
        return totalPrioridade;
    }

    public void setTotalPrioridade(Integer totalPrioridade) {
        this.totalPrioridade = totalPrioridade;
    }

    public void atualizaDataSincronizacaoSucesso(Date dataSincronizacao) {
        this.dataSincronizacao = dataSincronizacao;
        erroSincronizacao = 0;
    }

    public void atualizaDataSincronizacaoErro(Date dataSincronizacao) {
        this.dataSincronizacao = dataSincronizacao;
        erroSincronizacao = getErroSincronizacao()+1;
    }

    public Integer getErroSincronizacao() {
        return erroSincronizacao == null ? 0 : erroSincronizacao;
    }

    public void setErroSincronizacao(Integer erroSincronizacao) {
        this.erroSincronizacao = erroSincronizacao;
    }

    public void adicionaSegredoJustica(String motivoSegredoJustica) {
        this.motivoSegredoJustica = motivoSegredoJustica;
        setSegredoJustica(true);
    }

    public void paraIntegracao() {
        tarefaEnum = ParaIntegracao;
    }

    public void devolveOrigemSemManifestacao() {
        validaTarefa(ParaIntegracaoSemManifestacao);
        tarefaEnum = ParaIntegracaoSemManifestacao;
    }

    public void naoConcluso() throws Exception {
        TarefaEnum novaTarefa = NaoConcluso;
        if((tarefaEnum == ParaIntegracao || tarefaEnum == ParaIntegracaoComErro) && minutaEmElaboracao().isPresent()){
            novaTarefa = tarefaEnum;
        } else if (tarefaEnum == ParaIntegracaoAlvara || (tarefaEnum != ParaIntegracaoSemManifestacao && (tarefaEnum == ParaIntegracao || tarefaEnum == ParaIntegracaoComErro))) {
            Optional<Minuta> minutaComAlvara = getMinutaComAlvaraParaIntegracao();
            if (minutaComAlvara.isPresent() && !minutaComAlvara.get().getAlvara().foiIntegrado()) {
                novaTarefa = ParaIntegracaoAlvara;
            }
        }
        if(novaTarefa == NaoConcluso)
            removeTagsEfemeras("SISTEMA");
        tarefaEnum = novaTarefa;
    }

    public boolean temTag(String tag) {
        return tags != null && tags.stream().anyMatch(pt -> pt.getTag().getTag().equals(tag));
    }

    public void assinando(){
        validaTarefa(Assinando);
        tarefaEnum = Assinando;
    }

    public void removeSegredoJustica() {
        this.setSegredoJustica(false);
    }

    public void recusar() {
        if (this.getTarefa() != TarefaEnum.Assinar)
            throw new IllegalStateException("Processo não pode ser recusado pois não estava para Assinar");
        tarefaEnum = Corrigir;
    }

    public List<Parte> getPartes() {
        try {
            if(partes == null || partes.size() == 0)
                partes = this.processoPartes.stream().map(Parte::new).collect(Collectors.toList());
            return partes;
        }catch (Throwable t){
            return new ArrayList<>();
        }
    }

    public void setPartes(List<Parte> partes) {
        this.partes = partes;
    }

    public boolean minutaFoiIntegrado() {
        return minutas == null || minutas.size() == 0 || !minutas.stream().filter(m -> !m.foiIntegrada()).findAny().isPresent();
    }

    public void paraIntegracaoComErro() {
        this.tarefaEnum = ParaIntegracaoComErro;
    }

    public void tratarErroDevolucaoOrigem(String message) throws Exception {
        if (message == null)
            message = "";
        if (message.contains(SemMinutaParaIntegracaoEhProcessoNaoConcluso.toString())) {
            this.naoConcluso();
        }
        else if (message.contains(FalhaRecuperarAssinaturaStorage.toString()) || message.contains(SemMovimento.toString())) {
            this.reenviaProcessoParaAssinatura();
            this.tarefaEnum = Assinar;
        } else {
                this.paraIntegracaoComErro();
        }
    }

    private void reenviaProcessoParaAssinatura() throws Exception {
        Optional<Minuta> minuta = minutaEmElaboracao();
        if(minuta.isPresent())
            minuta.get().removeAssinatura();
        tarefaEnum = Assinar;
    }

    public Optional<Minuta> minutaEmElaboracao() throws Exception {
        List<Minuta> lista = minutas.stream().filter(p -> !p.foiIntegrada()).collect(toList());
        if (lista.size() == 1)
            return Optional.of(lista.get(0));
        else if (lista.size() == 0)
            return Optional.empty();
        throw  new Exception("Existe mais de uma minuta ativa para o processo: "+numeroProcesso);
    }

    @JsonIgnore
    public Optional<Minuta> getMinutaComAlvaraParaIntegracao() {
        List<Minuta> alvaras = getMinutas().stream().filter(p -> p.temAlvara() && !p.getAlvara().foiIntegrado()).collect(toList());
        if (alvaras.size() > 1)
            throw new IllegalStateException("Existe mais de um alvara pendente para o processo " + numeroProcesso);
        if (!minutaFoiIntegrado())
            throw new IllegalStateException("A minuta ainda não foi integrada " + numeroProcesso);
        else if (alvaras.size() == 1)
            return Optional.of(alvaras.get(0));
        return Optional.empty();
    }

    public void adicionaMinuta(Minuta minuta) throws Exception {
        if(minuta.foiIntegrada())
            throw new IllegalArgumentException("A minuta que esta sendo adicionada ja foi integrada");
        if(!minutaJaExiste(minuta) && !minutaEmElaboracao().isPresent())
            minutas.add(minuta);
        else if(!minutaJaExiste(minuta))
            throw new IllegalStateException("Já Existe uma minuta em elaboração");

    }

    private boolean minutaJaExiste(Minuta minuta){
        return (minutas.stream().filter(m-> m.getId() == minuta.getId()).collect(toList()).size() > 0);
    }

    public void paraIntegracaoSemManifestacao() {
        validaTarefa(ParaIntegracaoSemManifestacao);
        tarefaEnum = ParaIntegracaoSemManifestacao;
    }

    @JsonIgnore
    public String numeroProcessoSemFormatacao() {
        if (numeroProcesso != null && numeroProcesso.length() > 0)
            return numeroProcesso.replaceAll("[^0-9]", "");
        return numeroProcesso;
    }

    public Integer getManifestacao() {
        return caixa != null && caixa.getId() != null ? caixa.getId() : 0;
    }

    public String getManifestacaoDescricao() {
        return caixa != null && caixa.getNome() != null  ? caixa.getNome() : "";
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public Integer getTempoConcluso() {
        return tempoConcluso;
    }

    public void setTempoConcluso(Integer tempoConcluso) {
        this.tempoConcluso = tempoConcluso;
    }

    public List<ProcessoExpediente> getProcessoExpedientes() {
        return processoExpedientes;
    }

    public void setProcessoExpedientes(List<ProcessoExpediente> processoExpedientes) {
        this.processoExpedientes = processoExpedientes;
    }

    public void removeTagsEfemeras(String usuarioExclusao){
        if(tags != null) {
            tags.stream()
                .filter(t -> !t.isPersistente())
                .forEach(t -> t.deletar(usuarioExclusao));
        }
    }

    public String getMotivoSegredoJustica() {
        return motivoSegredoJustica;
    }

    public void setMotivoSegredoJustica(String motivoSegredoJustica) {
        this.motivoSegredoJustica = motivoSegredoJustica;
    }

    public List<TpuAssunto> getTpuAssuntos() {
        if(processoAssuntos == null)
            return new ArrayList<>();
        return processoAssuntos.stream().map(ProcessoAssunto::getAssuntoTpu).collect(toList());
    }

    public List<ProcessoTag> getTags() {
        return tags;
    }

    public void setTags(List<ProcessoTag> tags) {
        this.tags = tags;
    }

    public Long getIdClasseJudicial(){
        return idClasseJudicial;
    }

    public void setIdClasseJudicial(Long idClasseJudicial) {
        this.idClasseJudicial = idClasseJudicial;
    }

    public String getOrgaoJulgador() {
        if(orgaoJulgadorObj == null)
            return null;
        else
            return orgaoJulgadorObj.getId();
    }

    public Boolean isDigital() {
        return digital != null && digital;
    }

    public void setDigital(Boolean digital) {
        this.digital = digital;
    }

    public Optional<Integer> getNivelSigilo() {
        return Optional.ofNullable(nivelSigilo);
    }

    public void setNivelSigilo(Integer nivelSigilo) {
        this.nivelSigilo = nivelSigilo;
    }

    public Optional<String> getIdColegiado() {
        return idColegiado == null || idColegiado.isEmpty() ? Optional.empty() : Optional.of(idColegiado);
    }

    public void setIdColegiado(String idColegiado) {
        this.idColegiado = idColegiado;
    }

    public void removePrioridade() {
        prioridade = false;
        if(prioridades != null)
            prioridades.clear();
    }

    public void paraIntegracaoPauta() {
        this.tarefaEnum = ParaIntegracaoPauta;
    }

    public boolean verificaSigilo(Usuario usuario){
        return usuario.lePorcesso(this);
    }

    @Override
    public String toString() {
        return format("%s (%s)", numeroProcesso, id);
    }
}
