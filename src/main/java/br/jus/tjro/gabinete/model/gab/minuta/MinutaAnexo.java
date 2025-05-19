package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.infrastructure.converter.TipoDocumentoConverter;
import br.jus.tjro.gabinete.listener.kafka.dtos.PreliminarDTO;
import br.jus.tjro.gabinete.model.Assinavel;
import br.jus.tjro.gabinete.model.Renderizavel;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.enums.TipoAssinatura;
import br.jus.tjro.gabinete.model.gab.enums.TipoDocumentoAssinatura;
import br.jus.tjro.gabinete.model.gab.enums.TipoVotoPreliminar;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "MINUTA_ANEXO")
@SequenceGenerator(name = MinutaAnexo.SEQUENCE_NAME, sequenceName = MinutaAnexo.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MinutaAnexo implements Assinavel, Renderizavel {

    public static final String SEQUENCE_NAME = "SEQUENCIA_MINUTA_ANEXO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @JsonInclude(Include.NON_NULL)
    private String hash;

    @Column(name = "html")
    private String html;

    @Column(name = "hash_p7s")
    @JsonIgnore
    private String hashP7s;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "id_minuta", foreignKey = @ForeignKey(name = "FK_MINUTA_ANEXO_MINUTA"))
    @JsonIgnore
    private Minuta minutaPai;

    @Column(name = "tipo_documento")
    @Convert(converter = TipoDocumentoConverter.class)
    private TipoDocumento tipoDocumento;

    @Column(name= "tipo_voto")
    private TipoVotoPreliminar tipoVoto;

    @Column(name = "prejudica_merito")
    private Boolean prejudicaMerito = false;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "sigilo")
    private Boolean sigilo;

    @Column(name = "ID_MINUTA_SISTEMA_LEGADO")
    private String idMinutaSistemaLegado;

    @Column(name = "POSICAO")
    private Integer posicao;

    @Column(name = "tipo_assinatura")
    @Enumerated(EnumType.STRING)
    private TipoAssinatura tipoAssinatura = TipoAssinatura.Cms;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_assinatura", foreignKey = @ForeignKey(name = "FK_ASSINA_MINUTA_ANEXO"))
    private Assinatura assinatura;

    public MinutaAnexo() {
    }

    @JsonCreator
    public MinutaAnexo(@JsonProperty("tipoDocumento") String tipoDocumento) {
        this.tipoDocumento = new TipoDocumentoConverter().convertToEntityAttribute(tipoDocumento);
    }

    public MinutaAnexo(Minuta minuta, String hash, String contentType) {
        this.hash = hash;
        this.minutaPai = minuta;
        this.contentType = contentType;
    }

    public MinutaAnexo(Long id, String hash, String html, String hashP7s, String descricao, Minuta minutaPai,
                       TipoDocumento tipoDocumento, String contentType, Boolean sigilo, Integer posicao) {
        this.id = id;
        this.hash = hash;
        this.html = html;
        this.hashP7s = hashP7s;
        this.descricao = descricao;
        this.minutaPai = minutaPai;
        this.tipoDocumento = tipoDocumento;
        this.contentType = contentType;
        this.sigilo = sigilo;
        this.posicao = posicao;
    }

    public static void ordenaPorPosicao(List<MinutaAnexo> anexos) {
        if(anexos != null && anexos.size() > 0)
            Collections.sort(anexos, new Comparator<MinutaAnexo>() {
                public int compare(MinutaAnexo o1, MinutaAnexo o2) {
                    if (o1.getPosicao() == null || o2.getPosicao() == null)
                        return 0;
                    return o1.getPosicao().compareTo(o2.getPosicao());
                }
            });
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public TipoDocumentoAssinatura getTipoDocumentoAssinatura() {
        return TipoDocumentoAssinatura.Anexo;
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHashP7s() {
        return hashP7s;
    }

    public void setHashP7s(String hashP7s) {
        this.hashP7s = hashP7s;
    }

    public String getContentType() {
        return this.contentType == null ? "application/pdf" : this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getMinutaPaiTransient() {
        return minutaPai.getId();
    }

    public void setMinutaPaiTransient(Long id) {
        this.minutaPai = new Minuta(id);
    }

    public Minuta getMinutaPai() {
        return minutaPai;
    }

    public void setMinutaPai(Minuta minutaPai) {
        this.minutaPai = minutaPai;
    }

    public Long getIdMinutaPai() {
        return minutaPai.getId();
    }

    public void setIdMinutaPai(Long id) {
        this.minutaPai = new Minuta(id);
    }

    public String getIdMinutaSistemaLegado() {
        return idMinutaSistemaLegado;
    }

    public void setIdMinutaSistemaLegado(String idMinutaSistemaLegado) {
        if((idMinutaSistemaLegado == null || idMinutaSistemaLegado.equals("")) && !(this.idMinutaSistemaLegado == null || this.idMinutaSistemaLegado.equals(""))) {
            throw new RuntimeException("Você não pode setar null no idLegado do anexo");
        }
        this.idMinutaSistemaLegado = idMinutaSistemaLegado;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public String getExtensao() {
        if ("application/pdf".equals(getContentType()))
            return ".pdf";
        else
            return ".txt";
    }

    public Boolean isSigilo() {
        return sigilo != null && sigilo;
    }

    public boolean foiIntegrado() {
        return (this.isAssinado() || assinatura!=null) && idMinutaSistemaLegado != null && !idMinutaSistemaLegado.equals("");
    }

    private boolean isAssinado() {
        return ofNullable(hashP7s).map(h -> !h.equals("")).orElse(false);
    }

    public void removeAssinatura() {
        hashP7s = null;
        idMinutaSistemaLegado = null;
    }

    public Boolean getSigilo() {
        return sigilo;
    }

    public void setSigilo(Boolean sigilo) {
        this.sigilo = sigilo;
    }

    @JsonIgnore
    public boolean isValid() {
        return minutaPai != null &&
            tipoDocumento != null &&
            !isNullOrEmpty(descricao) &&
            (!isNullOrEmpty(hash) || !isNullOrEmpty(html));
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public boolean temConteudo() {
        //TODO adicionar no futuro uma verificação para o html renderizado em relação ao acordao.
        return hash != null;
    }

    @Override
    public String getTemplate() {
        return this.html;
    }

    @Override
    public void setHtmlProcessado(String htmlProcessado) {
        this.html = htmlProcessado;
    }

    public TipoVotoPreliminar getTipoVoto() {
        return tipoVoto;
    }

    public void setTipoVoto(TipoVotoPreliminar tipoVotoPreliminar) {
        this.tipoVoto = tipoVotoPreliminar;
    }

    public Boolean getPrejudicaMerito() {
        return prejudicaMerito;
    }

    public void setPrejudicaMerito(Boolean prejudicaMerito) {
        this.prejudicaMerito = prejudicaMerito;
    }

    public PreliminarDTO getDTO() {
        return new PreliminarDTO(this.getId(), this.getTipoVoto(), this.getPosicao(), this.getPrejudicaMerito());
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
}
