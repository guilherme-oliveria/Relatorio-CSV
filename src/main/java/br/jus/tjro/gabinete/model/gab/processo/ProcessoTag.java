package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.interfaces.importacao.EntidadeDoisBancos;
import br.jus.tjro.gabinete.model.gab.tag.Tag;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Where;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = ProcessoTag.NomeTabela,
    uniqueConstraints = { @UniqueConstraint(columnNames = { ProcessoTag.NomeColunaIdProcesso, ProcessoTag.NomeColunaIdTag, ProcessoTag.NomeColunaDataExclusao  }, name = "UK_PROCESSO_TAG") },
    indexes = {@Index(columnList = ProcessoTag.NomeColunaIdProcesso, name = "idx_id_processo")}
)
@SequenceGenerator(
    name = ProcessoTag.SEQUENCE_NAME,
    sequenceName = ProcessoTag.SEQUENCE_NAME,
    allocationSize = 1)
@Where(clause = ProcessoTag.NomeColunaDataExclusao + " is null")
public class ProcessoTag  implements EntidadeDoisBancos {

    public static final String SEQUENCE_NAME = "SEQUENCIA_PROCESSO_TAG";
    public static final String NomeColunaIdProcesso = "id_processo";
    public static final String NomeColunaIdTag = "id_tag";
    public static final String NomeColunaDataExclusao = "data_exclusao";
    public static final String NomeTabela = "PROCESSO_TAG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @Where(clause = Tag.NomeColunaDataExclusao + " is null")
    @JoinColumn(name = NomeColunaIdProcesso, foreignKey = @ForeignKey(name = "FK_TAG_PROCESSO"))
    @NotNull(message = "Processo não pode ser nulo")
    @JsonBackReference
    private Processo processo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = NomeColunaIdTag, foreignKey = @ForeignKey(name = "FK_TAG"))
    private Tag tag;

    @Column(name = "persistente")
    private boolean persistente;

    @Column(name = NomeColunaDataExclusao)
    private Date dataExclusao;

    @Column(name = "usuario_exclusao")
    private String usuarioExclusao;


    public ProcessoTag(){}

    public ProcessoTag(Tag tag,boolean persistente, Processo processo){
        this.tag = tag;
        this.persistente = persistente;
        this.processo = processo;
    }

    public ProcessoTag(ProcessoTag processoTag, Processo processo) {
        this.id = processoTag.getId();
        this.processo = processo;
        if(processo.getTags() instanceof List)
            processo.getTags().add(this);
        this.tag = processoTag.getTag();
        this.dataExclusao = processoTag.getDataExclusao();
        this.usuarioExclusao = processoTag.getUsuarioExclusao();
        this.persistente = processoTag.isPersistente();
    }

    public void deletar(String usuarioExclusao){
        if(dataExclusao == null) {
            this.usuarioExclusao = usuarioExclusao.length() > 20 ? usuarioExclusao.substring(0,19) : usuarioExclusao;
            this.dataExclusao = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        }
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

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public boolean isPersistente() {
        return persistente;
    }

    public void setPersistente(boolean persistente) {
        this.persistente = persistente;
    }

    public Date getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(Date dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public String getUsuarioExclusao() {
        return usuarioExclusao;
    }

    public void setUsuarioExclusao(String usuarioExclusao) {
        this.usuarioExclusao = usuarioExclusao;
    }

    @Override
    public String getObjetoKeyString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTag().getIdTagPje());
        return sb.toString();
    }

    @Override
    public String getObjetoUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTag().getTag());
        return sb.toString();
    }

}
