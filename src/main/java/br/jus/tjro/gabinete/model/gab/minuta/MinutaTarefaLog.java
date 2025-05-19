package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.Usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MINUTA_TAREFA_LOG")
@SequenceGenerator(name = MinutaTarefaLog.SEQUENCE_NAME, sequenceName = MinutaTarefaLog.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class MinutaTarefaLog {

    public static final String SEQUENCE_NAME = "SEQUENCIA_MINUTA_TAREFA_LOG";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_minuta", foreignKey = @ForeignKey(name = "FK_MINUTA_TAREFA_LOG"))
    @JsonBackReference
    private Minuta minuta;

    @Column(name = "data_log")
    private Date dataLog;

    @Column(name = "nome_autor")
    private String nomeAutor;

    @Column(name = "autor_versao")
    private String autorVersao;

    @Column(name = "tarefaenum")
    @Enumerated(EnumType.STRING)
    private TarefaEnum tarefaEnum = TarefaEnum.Minutar;

    public MinutaTarefaLog() { }

    public MinutaTarefaLog(Minuta minuta, Usuario usuario, TarefaEnum tarefaEnum) {
        this.minuta = minuta;
        this.nomeAutor = usuario.getNome();
        this.dataLog = new Date();
        this.autorVersao = usuario.getId();
        this.tarefaEnum = tarefaEnum;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Minuta getMinuta() {
        return minuta;
    }

    public void setMinuta(Minuta minuta) {
        this.minuta = minuta;
    }

    public Date getDataLog() {
        return dataLog;
    }

    public void setDataLog(Date dataLog) {
        this.dataLog = dataLog;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public String getAutorVersao() {
        return autorVersao;
    }

    public void setAutorVersao(String autorVersao) {
        this.autorVersao = autorVersao;
    }

    public TarefaEnum getTarefaEnum() {
        return tarefaEnum;
    }

    public void setTarefaEnum(TarefaEnum tarefaEnum) {
        this.tarefaEnum = tarefaEnum;
    }
}
