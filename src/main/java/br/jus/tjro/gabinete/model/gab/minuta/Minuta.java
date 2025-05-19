package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.infrastructure.converter.QuestionConverter;
import br.jus.tjro.gabinete.infrastructure.converter.TipoDocumentoConverter;
import br.jus.tjro.gabinete.model.Assinavel;
import br.jus.tjro.gabinete.model.Renderizavel;
import br.jus.tjro.gabinete.model.gab.alvara.Alvara;
import br.jus.tjro.gabinete.model.gab.alvara.PagamentoAlvara;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.enums.TipoAssinatura;
import br.jus.tjro.gabinete.model.gab.enums.TipoDocumentoAssinatura;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.TipoAnexo;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.question.QuestionBase;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "MINUTA")
@SequenceGenerator(name = Minuta.SEQUENCE_NAME, sequenceName = Minuta.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"},ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Minuta implements Serializable, Assinavel, Renderizavel {

    public static final String SEQUENCE_NAME = "SEQUENCIA_MINUTA";
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @NotNull
    private Long id;

    @Column(name = "minuta_html", columnDefinition="TEXT")
    private String minutaHtml;

    @Column(name = "minuta_html_renderizado", columnDefinition="TEXT")
    private String minutaHtmlRenderizado;

    @Column(name = "hash_p7s")
    @JsonIgnore
    private String hashP7s;

    @Column(name = "tipo_assinatura")
    @Enumerated(EnumType.STRING)
    private TipoAssinatura tipoAssinatura = TipoAssinatura.Cms;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_assinatura", foreignKey = @ForeignKey(name = "FK_ASSINATURA_MINUTA"))
    private Assinatura assinatura;

    @Column(name = "ID_TIPO_DOCUMENTO")
    @Convert(converter = TipoDocumentoConverter.class)
    private TipoDocumento tipoDocumento;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "minutaPai")
    @JsonManagedReference
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<MinutaAnexo> anexos;

    @OneToMany(mappedBy = "minuta", cascade = CascadeType.MERGE)
    @JsonManagedReference
    private List<MinutaMovimento> minutaMovimentos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_processo", foreignKey = @ForeignKey(name = "FK_MINUTA_PROCESSO"))
    @NotNull(message = "Processo não pode ser nulo")
    private Processo processo;

    @Column(name = "ID_MINUTA_SISTEMA_LEGADO")
    private String idMinutaSistemaLegado;

    @Column(name = "VERSAO")
    private Integer versao;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "minuta", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MinutaVersao> versoes;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "minuta", cascade = {CascadeType.ALL})
    @JsonManagedReference
    private List<PublicarProcessoDJE> publicacaoDje;

    @Column(name = "eh_sigiloso")
    private Boolean ehSigiloso;

    @OneToOne(mappedBy = "minuta")
    @JsonBackReference
    private Alvara alvara;

    @Transient
    private String autor;

    @Transient
    private String nomeAutor;

    @Column(name = "HA_OUTRAS_COMUNICACOES")
    private Boolean haOutrasComunicacoes = false;

    @Column(name = "EM_FASE_FINALIZACAO")
    private Boolean emFaseFinalizacao = false;

    @Column(name = "COVID")
    private boolean covid = false;

    @Transient
    private Date dataJuntada;

    @Transient
    private String descricao;

    @Transient
    private String dsTipoAnexo;

    @Transient
    private String dsInstancia;

    @Transient
    private String nomeUserInclusao;

    @Transient
    private String extensao;

    @Transient
    private TipoAnexo tipoAnexo;

    @Transient
    @JsonProperty(value = "idStorage")
    private String nrDocumentoStorage;

    @Transient
    private String dsMd5Documento;

    @Transient
    private byte[] arquivoAssinado;

    @OneToOne(mappedBy = "minuta", cascade = {CascadeType.ALL})
    @JsonManagedReference
    private PedidoInclusaoPauta pedidoInclusaoPauta;

    @Column(name = "tipos_documentos", length = 1000)
    private String tiposDocumentos;

    public Minuta() {
    }

    @JsonCreator
    public Minuta(@JsonProperty("idProcesso") long processo,
                  @JsonProperty("tipoDocumento") TipoDocumento tipoDocumento) throws Exception {
        if (processo == 0) {
            throw new RuntimeException("Operação abortada, não foi encontrado referencia para o ID do Processo");
        }
        this.processo = new Processo(processo);
        this.tipoDocumento = tipoDocumento;
    }

    public Minuta(Processo processo) {
        this.processo = processo;
    }

    public Minuta(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Processo getProcesso() {
        return processo;
    }

    public Long getIdProcesso() {
        return processo != null ? processo.getId() : null;
    }

    //TODO O front end envia o id do processo para quando for fazer a persistencia relacionar com o objeto correto. O ideia seria o front end enviar o objeto processo no payload do post
    @Deprecated
    public void setIdProcesso(Long idProcesso) {
        this.processo = new Processo(idProcesso);
    }

    public String getMinutaHtml() {
        return minutaHtml;
    }

    public void setMinutaHtml(String minutaHtml) {
        this.minutaHtml = minutaHtml;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @JsonIgnore
    public List<MinutaAnexo> getAnexos() {
        return anexos == null ? List.of() : anexos;
    }

    @JsonIgnore
    public List<MinutaAnexo> getAnexosExcetoPreliminares() {
        return getAnexos().stream()
            .filter(a -> !a.getTipoDocumento().isPreliminar())
            .collect(Collectors.toList());
    }

    @JsonIgnore
    public List<MinutaAnexo> getPreliminares() {
        return getAnexos().stream()
            .filter(minutaAnexo -> minutaAnexo.getTipoDocumento().isPreliminar())
            .collect(Collectors.toList());
    }

    public void setAnexos(List<MinutaAnexo> anexos) {
        this.anexos = anexos;
    }

    public String getHashP7s() {
        return hashP7s;
    }

    public void setHashP7s(String hashP7s) {
        this.hashP7s = hashP7s;
    }

    public String getIdMinutaSistemaLegado() {
        return idMinutaSistemaLegado;
    }

    public void setIdMinutaSistemaLegado(String idMinutaSistemaLegado) {
        if((idMinutaSistemaLegado == null || idMinutaSistemaLegado.equals("")) && !(this.idMinutaSistemaLegado == null || this.idMinutaSistemaLegado.equals(""))) {
            throw new RuntimeException("Você não pode setar null no idLegado da minuta");
        }
        this.idMinutaSistemaLegado = idMinutaSistemaLegado;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public List<MinutaVersao> getVersoes() {
        if(versoes != null) {
            versoes.sort((o1, o2) -> {
                if (o1.getVersao() == null || o2.getVersao() == null)
                    return 0;
                return o2.getVersao().compareTo(o1.getVersao());
            });
            return versoes;
        }
        return new ArrayList<MinutaVersao>();
    }

    public void setVersoes(List<MinutaVersao> versoes) {
        this.versoes = versoes;
    }

    public PublicarProcessoDJE getPublicacaoDje() {
        if(publicacaoDje != null && publicacaoDje.size() > 0)
            return publicacaoDje.get(publicacaoDje.size()-1);
        else
            return null;
    }

    public void setPublicacaoDje(PublicarProcessoDJE publicacaoDje) {
        this.publicacaoDje = new ArrayList<>();
        this.publicacaoDje.add(publicacaoDje);
    }

    public Boolean getEhSigiloso() {
        return ehSigiloso;
    }

    public void setEhSigiloso(Boolean ehSigiloso) {
        this.ehSigiloso = ehSigiloso;
    }

    public Boolean getHaOutrasComunicacoes() {
        return haOutrasComunicacoes;
    }

    public void setHaOutrasComunicacoes(Boolean haOutrasComunicacoes) {
        this.haOutrasComunicacoes = haOutrasComunicacoes;
    }

    public String getMinutaHtmlRenderizado() {
        return minutaHtmlRenderizado;
    }

    public void setMinutaHtmlRenderizado(String minutaHtmlRenderizado) {
        this.minutaHtmlRenderizado = minutaHtmlRenderizado;
    }

    @JsonIgnore
    public InputStream getMinutaHtmlRenderizadoInputStream() throws UnsupportedEncodingException {
        return new ByteArrayInputStream(getMinutaHtmlRenderizadoBytes());
    }

    @JsonIgnore
    public byte[] getMinutaHtmlRenderizadoBytes() throws UnsupportedEncodingException {
        if(this.minutaHtmlRenderizado != null)
            return this.minutaHtmlRenderizado.getBytes(StandardCharsets.UTF_8.name());
        else
            return null;
    }

    public List<MinutaMovimento> getMinutaMovimentos() {
        return minutaMovimentos;
    }

    @Deprecated
    public void setMinutaMovimentos(List<MinutaMovimento> minutaMovimentos) {
        this.minutaMovimentos = minutaMovimentos;
    }

    public Boolean getEmFaseFinalizacao() {
        return emFaseFinalizacao;
    }

    public void setEmFaseFinalizacao(Boolean emFaseFinalizacao) {
        this.emFaseFinalizacao = emFaseFinalizacao;
    }

    public boolean isCovid() {
        return covid;
    }

    public void setCovid(boolean covid) {
        this.covid = covid;
    }

    public Date getDataJuntada() {
        return dataJuntada;
    }

    public void setDataJuntada(Date dataJuntada) {
        this.dataJuntada = dataJuntada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDsTipoAnexo() {
        return dsTipoAnexo;
    }

    public void setDsTipoAnexo(String dsTipoAnexo) {
        this.dsTipoAnexo = dsTipoAnexo;
    }

    public String getDsInstancia() {
        return dsInstancia;
    }

    public void setDsInstancia(String dsInstancia) {
        this.dsInstancia = dsInstancia;
    }

    public String getNomeUserInclusao() {
        return nomeUserInclusao;
    }

    public void setNomeUserInclusao(String nomeUserInclusao) {
        this.nomeUserInclusao = nomeUserInclusao;
    }

    public String getExtensao() {
        return extensao;
    }

    public void setExtensao(String extensao) {
        this.extensao = extensao;
    }

    public TipoAnexo getTipoAnexo() {
        return tipoAnexo;
    }

    public void setTipoAnexo(TipoAnexo tipoAnexo) {
        this.tipoAnexo = tipoAnexo;
    }

    public String getNrDocumentoStorage() {
        return nrDocumentoStorage;
    }

    public void setNrDocumentoStorage(String nrDocumentoStorage) {
        this.nrDocumentoStorage = nrDocumentoStorage;
    }

    public String getDsMd5Documento() {
        return dsMd5Documento;
    }

    public void setDsMd5Documento(String dsMd5Documento) {
        this.dsMd5Documento = dsMd5Documento;
    }

    public byte[] getMinutaHtmlRenderizadoEhAssinadoBytes() {
        return this.arquivoAssinado;
    }

    public void setMinutaHtmlRenderizadoEhAssinadoBytes(byte[] arquivoAssinado) {
        this.arquivoAssinado = arquivoAssinado;
    }

    public Optional<PedidoInclusaoPauta> getPedidoInclusaoPauta() {
        return Optional.ofNullable(pedidoInclusaoPauta);
    }

    @JsonIgnore
    public boolean isAssinaPauta() throws Exception {
        if(tipoDocumento != null && tipoDocumento.isMinutaColegiado() && this.getPedidoInclusaoPauta().isEmpty())
            throw new Exception("Não foi encontrado pauta para verificar a assinatura");
        return tipoDocumento != null &&
            tipoDocumento.isMinutaColegiado() &&
            this.getPedidoInclusaoPauta().isPresent() &&
            !this.getPedidoInclusaoPauta().get().foiRealizado();
    }

    public void setPedidoInclusaoPauta(PedidoInclusaoPauta pedidoInclusaoPauta) {
        this.pedidoInclusaoPauta = pedidoInclusaoPauta;
    }

    public Alvara getAlvara() {
        return alvara;
    }

    public void setAlvara(Alvara alvara) {
        this.alvara = alvara;
    }

    @Transient
    public Boolean foiIntegrada(){
        return  idMinutaSistemaLegado != null && !idMinutaSistemaLegado.equals("") && (anexos == null || (anexos.size() == 0 || anexos.stream().allMatch(a -> a.foiIntegrado())));
    }

    public void removeAssinatura() {
        hashP7s = null;
        idMinutaSistemaLegado = null;
        assinatura = null;
        if(anexos != null)
            anexos.forEach(a -> a.removeAssinatura());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Minuta minuta = (Minuta) o;
        return Objects.equals(id, minuta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getUltimaAlteracao() {
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        List<MinutaVersao> versoes = getVersoes();
        return versoes.stream()
            .map(minutaVersao -> String.format("Última alteração por %s em %s", minutaVersao.getNomeAutor(), dt.format(minutaVersao.getDataVersao())))
            .findFirst().orElse("");
    }

    public void setArquivoAssinado(byte[] arquivoAssinado) {
        this.arquivoAssinado = arquivoAssinado;
    }

    public byte[] getArquivoAssinado() {
        return arquivoAssinado;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public TipoAssinatura getTipoAssinatura() {
        return tipoAssinatura;
    }

    public void setTipoAssinatura(TipoAssinatura tipoAssinatura) {
        this.tipoAssinatura = tipoAssinatura;
    }

    public Assinatura getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(Assinatura assinatura) {
        this.assinatura = assinatura;
    }

    public boolean temAlvara() {
        if (alvara != null && this.alvara.isAtivo()) {
            List<PagamentoAlvara> pagamentoAlvaras = this.alvara.getPagamentoAlvara().stream().filter(PagamentoAlvara::isAtivo).collect(Collectors.toList());
            return pagamentoAlvaras.size() > 0;
        }
        return false;
    }

    public void copiaPropriedades(Minuta minutaFake) {
        setMinutaHtml(minutaFake.getMinutaHtml());
        setTipoDocumento(minutaFake.getTipoDocumento());
        setEmFaseFinalizacao(minutaFake.getEmFaseFinalizacao());
        setHaOutrasComunicacoes(minutaFake.getHaOutrasComunicacoes());
    }

    public void defineAutoria(Usuario usuario) {
        setAutor(usuario.getCpf());
        setNomeAutor(usuario.getNome());
    }

    public void descartar() {
        idMinutaSistemaLegado = "-1";
    }

    @Column(name = "QUESTIONS")
    @JsonInclude(Include.NON_NULL)
    @Convert(converter = QuestionConverter.class)
    private Set<QuestionBase> questions;

    public Set<QuestionBase> getQuestions() {
        if(questions != null)
            return questions.stream().filter(it -> it.activeBy(processo)).collect(Collectors.toSet());
        else
            return new HashSet<>();
    }

    public void setQuestions(Set<QuestionBase> questions) {
        this.questions = questions;
    }

    @Override
    public TipoDocumentoAssinatura getTipoDocumentoAssinatura() {
        return TipoDocumentoAssinatura.Principal;
    }

    @Override
    public String getTemplate() {
        return this.minutaHtml;
    }

    @Override
    public void setHtmlProcessado(String htmlProcessado) {
        this.minutaHtml = htmlProcessado;
        this.minutaHtmlRenderizado = htmlProcessado;
    }

    public String getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(String tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

}
