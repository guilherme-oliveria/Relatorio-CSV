package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.interfaces.ModeloDocumentoService;
import br.jus.tjro.gabinete.model.Assinavel;
import br.jus.tjro.gabinete.model.Renderizavel;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.enums.TipoDocumentoAssinatura;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Entity
@Table(name = "PEDIDO_INCLUSAO_PAUTA")
@SequenceGenerator(name = PedidoInclusaoPauta.SEQUENCE_NAME, sequenceName = PedidoInclusaoPauta.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"},ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PedidoInclusaoPauta implements Serializable, Assinavel, Renderizavel {
    public static final String SEQUENCE_NAME = "SEQ_PEDIDO_INCLUSAO_PAUTA";
    private static final long serialVersionUID = 1L;

    public PedidoInclusaoPauta(){}

    public PedidoInclusaoPauta(Minuta minuta) {
        this.minuta = minuta;
    }

    public PedidoInclusaoPauta(Long id, String htmlRenderizado, String hashP7s, Minuta minuta, Date dataAssinatura) {
        this.id = id;
        this.htmlRenderizado = htmlRenderizado;
        this.hashP7s = hashP7s;
        this.minuta = minuta;
        this.dataAssinatura = dataAssinatura;
    }

    public PedidoInclusaoPauta(String html) {
        this.htmlRenderizado = html;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @NotNull
    private Long id;

    @Column(name = "html_renderizado")
    private String htmlRenderizado;

    @Column(name = "hash_p7s")
    @JsonIgnore
    private String hashP7s;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ID_MINUTA", referencedColumnName = "id")
    @JsonBackReference
    @NotNull(message = "Minuta não pode ser nulo")
    private Minuta minuta;

    @Column(name = "data_assinatura")
    private Date dataAssinatura;

    @Column(name = "id_sistema_legado")
    private String idSistemaLegado;

    @Override
    public TipoDocumentoAssinatura getTipoDocumentoAssinatura() {
        return TipoDocumentoAssinatura.PedidoInclusaoPauta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHtmlRenderizado() {
        return htmlRenderizado;
    }

    public void setHtmlRenderizado(String htmlRenderizado) {
        this.htmlRenderizado = htmlRenderizado;
    }

    public String getHashP7s() {
        return hashP7s;
    }

    public void setHashP7s(String hashP7s) {
        this.hashP7s = hashP7s;
    }

    public Minuta getMinuta() {
        return minuta;
    }

    public void setMinuta(Minuta minuta) {
        this.minuta = minuta;
    }

    public Date getDataAssinatura() {
        return dataAssinatura;
    }

    public void setDataAssinatura(Date dataAssinatura) {
        this.dataAssinatura = dataAssinatura;
    }

    public String getIdSistemaLegado() {
        return idSistemaLegado;
    }

    public void setIdSistemaLegado(String idSistemaLegado) {
        this.idSistemaLegado = idSistemaLegado;
    }

    @JsonIgnore
    public InputStream getHtmlRenderizadoInputStream() throws UnsupportedEncodingException {
        return new ByteArrayInputStream(getHtmlRenderizadoBytes());
    }

    @JsonIgnore
    public byte[] getHtmlRenderizadoBytes() throws UnsupportedEncodingException {
        return this.htmlRenderizado.getBytes(StandardCharsets.UTF_8.name());
    }

    @Transient
    @JsonIgnore
    public boolean foiRealizado() {
        return idSistemaLegado != null;
    }

    @Transient
    @JsonIgnore
    public boolean isAssinado(){
        return getHashP7s() != null && !getHashP7s().equals("") && getDataAssinatura() != null;
    }

    @Override
    public String getTemplate() {
        return htmlRenderizado;
    }

    @Override
    public void setHtmlProcessado(String htmlProcessado) {
        this.htmlRenderizado = htmlProcessado;
    }

    @Override
    public void renderizarHtml(ModeloDocumentoService service, Usuario usuario, Processo processo, Boolean assinando) throws ServicoRemotoException {
        String modeloProcessado = service.renderizarVariavel(usuario, processo, this.getTemplate(), assinando);
        this.setHtmlProcessado(modeloProcessado);
        minuta.renderizarHtml(service,usuario,processo,assinando);
        minuta.getAnexos().forEach(it -> {
            try {
                it.renderizarHtml(service,usuario,processo,assinando);
            } catch (ServicoRemotoException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public TipoDocumento getTipoDocumento() {
        return new TipoDocumento("63","Despacho");
    }
}
