package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.infrastructure.converter.TipoDocumentoConverter;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "processo_documento", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ID_DOCUMENTO_SISTEMA_LEGADO", "ID_PROCESSO"})})
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@SequenceGenerator(name = ProcessoDocumento.SEQUENCE_NAME, sequenceName = ProcessoDocumento.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class ProcessoDocumento {

    static final String SEQUENCE_NAME = "SEQUENCIA_PROCESSO_DOCUMENTO";

    // TODO documentos com in_valido = true / assinados

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    // nm_arquivo
    @Column(length = 100)
    private String descricao;

    @Column(name = "documento_html")
    private String documentoHtml;

    @NotNull
    @Column(name = "ID_DOCUMENTO_SISTEMA_LEGADO", length = 10)
    private String idDocumentoSistemaLegado;

    @NotNull
    @Column(name = "data_juntada")
    private Date dataJuntada;

    @Column(name="NOMEUSERINCLUSAO")
    private String nomeUserInclusao;

    @Column(name="NOMEUSERASSINATURA")
    private String nomeUserAssinatura;

    @Column(name = "extensao", length = 50)
    private String extensao;

    @Column(name = "eh_sigiloso")
    private Boolean ehSigiloso;

    @Column(name = "hash_storage", length = 64)
    private String hash;

    @Column(name = "ID_TIPO_DOCUMENTO")
    @Convert(converter = TipoDocumentoConverter.class)
    private TipoDocumento tipoDocumento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "id_processo", foreignKey = @ForeignKey(name = "FK_PROCESSO_DOCUMENTO_PROCESSO"))
    @NotNull(message = "Processo não pode ser nulo")
    private Processo processo;

    @Column(name = "hash_thumbnail", length = 64)
    private String hashThumbnail;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "id_usuario_exclusao")
    private Long idUsuarioExclusao;

    @Column(name = "motivo_exclusao")
    private String motivoExclusao;

    @Column(name = "data_exclusao")
    private Date dataExclusao;

    @Column(name = "nr_ordem")
    private Integer nrOrdem;

    @Transient
    private byte[] anexo;

    @Transient
    private String idProcesso;


    public ProcessoDocumento(Long idDocumentoSistemaLegado, Processo processo, String descricao, String documentoHtml,
                             Date dataJuntada, String nomeUserInclusao, Boolean ehSigiloso,
                             String extensao, TipoDocumento tipoDocumento,
                             Long idUsuarioExclusao, String motivoExclusao, Date dataExclusao, Boolean ativo,
                             Integer nrOrdem, String hash ) {
        this.idDocumentoSistemaLegado = String.valueOf(idDocumentoSistemaLegado);
        this.processo = processo;
        this.descricao = descricao;
        this.documentoHtml = documentoHtml;
        this.dataJuntada = dataJuntada;
        this.nomeUserInclusao = nomeUserInclusao;
        this.ehSigiloso = ehSigiloso;
        this.extensao = extensao;
        this.tipoDocumento = tipoDocumento;
        this.idUsuarioExclusao = idUsuarioExclusao;
        this.motivoExclusao = motivoExclusao;
        this.dataExclusao = dataExclusao;
        this.ativo = ativo;
        this.nrOrdem = nrOrdem;
        this.hash = hash;
    }

    public ProcessoDocumento() {
    }

    public ProcessoDocumento(Long id, String descricao, String extensao, Date dataJuntada) {
        this.id = id;
        this.descricao = descricao;
        this.extensao = extensao;
        this.dataJuntada = dataJuntada;
    }

    public static void ordenaPorDataJuntada(List<ProcessoDocumento> documentos) {
        documentos.sort((o1, o2) -> {
            if (o1.getDataJuntada().equals(o2.getDataJuntada()))
                return o1.getId().compareTo(o2.getId());
            return o1.getDataJuntada().compareTo(o2.getDataJuntada());
        });
    }

    public static void ordenaPorDataJuntadaAndIdLegado(List<ProcessoDocumento> documentos) {
        documentos.sort((o1, o2) -> {
            if (o1.getDataJuntada().equals(o2.getDataJuntada())) {
                return o1.getIdDocumentoSistemaLegado().compareTo(o2.getIdDocumentoSistemaLegado());
            }
            return o1.getDataJuntada().compareTo(o2.getDataJuntada());
        });
    }

    public static void ordenaPorDataJuntadaAndNrOrdem(List<ProcessoDocumento> documentos) {
        documentos.sort((o1, o2) -> {
            if (o1.getDataJuntada().equals(o2.getDataJuntada())) {
                if (o1.getNrOrdem().equals(o2.getNrOrdem())) {
                    return o1.getIdDocumentoSistemaLegado().compareTo(o2.getIdDocumentoSistemaLegado());
                }
                return o1.getNrOrdem().compareTo(o2.getNrOrdem());
            }
            return o1.getDataJuntada().compareTo(o2.getDataJuntada());
        });
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDocumentoHtml() {
        return documentoHtml;
    }

    public void setDocumentoHtml(String documentoHtml) {
        this.documentoHtml = documentoHtml;
    }

    public String getIdDocumentoSistemaLegado() {
        return idDocumentoSistemaLegado;
    }

    public void setIdDocumentoSistemaLegado(String idDocumentoSistemaLegado) {
        this.idDocumentoSistemaLegado = idDocumentoSistemaLegado;
    }

    public Date getDataJuntada() {
        return dataJuntada;
    }

    public void setDataJuntada(Date dataJuntada) {
        this.dataJuntada = dataJuntada;
    }

    public String getDataJuntadaFormatada() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formato.format(dataJuntada);
    }

    public String getNomeUserInclusao() {
        return nomeUserInclusao == null ? "" : nomeUserInclusao;
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

    public Boolean getEhSigiloso() {
        return ehSigiloso;
    }

    public void setEhSigiloso(Boolean ehSigiloso) {
        this.ehSigiloso = ehSigiloso;
    }

    public byte[] getAnexo() {
        return anexo;
    }

    public void setAnexo(byte[] anexo) {
        this.anexo = anexo;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public String getDsTipoDoc() {
        return tipoDocumento == null ? "" : tipoDocumento.getDescricao();
    }

    public String getFonteDados() {
        return processo != null ? processo.getSistema().toString() : null;
    }

    @Override
    public String toString() {
        return "" +
            "ID: " + getIdDocumentoSistemaLegado() + " em " + getDataJuntadaFormatada() +
            "\n" +
            "Documento: " + getDescricao() +
            "\n" +
            "Assinado eletronicamente por: " + getNomeUserAssinatura();
    }

    public Boolean getAtivo() {
        return ativo != null ? ativo : true;
    }

    public Integer getNrOrdem() {
        if (nrOrdem == null ) {
            return 0;
        } else {
            return nrOrdem;
        }
    }

    public void setHashThumbnail(String hashThumbnail) {
        this.hashThumbnail = hashThumbnail;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public void setIdProcesso(String idProcesso) {
        this.idProcesso = idProcesso;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public boolean isHtml(){
        return documentoHtml != null && !documentoHtml.equals("");
    }

    @Deprecated
    public void setPropriedadesRemota(ProcessoDocumento remoto, Processo processo) {
        this.idDocumentoSistemaLegado = String.valueOf(remoto.getIdDocumentoSistemaLegado());
        this.processo = processo;
        this.descricao = remoto.getDescricao();
        this.documentoHtml = remoto.getDocumentoHtml();
        this.dataJuntada = remoto.getDataJuntada();
        this.nomeUserInclusao = remoto.getNomeUserInclusao();
        this.ehSigiloso = remoto.getEhSigiloso();
        this.tipoDocumento = remoto.getTipoDocumento();
        this.extensao = remoto.getExtensao();
        this.descricao = remoto.getDescricao();
        this.idUsuarioExclusao = remoto.getIdUsuarioExclusao();
        this.motivoExclusao = remoto.getMotivoExclusao();
        this.dataExclusao = remoto.getDataExclusao();
        this.ativo = remoto.getAtivo();
        this.nrOrdem = remoto.getNrOrdem();
        this.hash = remoto.getHash();
    }

    public String getHash() {
        return hash;
    }

    public void setHash( String hash) {
        this.hash = hash;
    }

    public Processo getProcesso() {
        return processo;
    }

    public String getHashThumbnail() {
        return hashThumbnail;
    }

    public Long getIdUsuarioExclusao() {
        return idUsuarioExclusao;
    }

    public String getMotivoExclusao() {
        return motivoExclusao;
    }

    public Date getDataExclusao() {
        return dataExclusao;
    }

    public String getIdProcesso() {
        return idProcesso;
    }

    public String getNomeUserAssinatura() {
        return nomeUserAssinatura;
    }

    public void setNomeUserAssinatura(String nomeUserAssinatura) {
        this.nomeUserAssinatura = nomeUserAssinatura;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setIdUsuarioExclusao(Long idUsuarioExclusao) {
        this.idUsuarioExclusao = idUsuarioExclusao;
    }

    public void setMotivoExclusao(String motivoExclusao) {
        this.motivoExclusao = motivoExclusao;
    }

    public void setDataExclusao(Date dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public void setNrOrdem(Integer nrOrdem) {
        this.nrOrdem = nrOrdem;
    }
}
