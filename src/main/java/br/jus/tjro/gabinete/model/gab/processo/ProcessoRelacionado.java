package br.jus.tjro.gabinete.model.gab.processo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;


@Entity
@Table(name = "processo_relacionado")
@SequenceGenerator(name = ProcessoRelacionado.SEQUENCE_NAME, sequenceName = ProcessoRelacionado.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class ProcessoRelacionado {

    static final String SEQUENCE_NAME = "SEQUENCIA_PROCESSO_RELACIONADO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_processo", foreignKey = @ForeignKey(name = "FK_PROC_REL_PROCESSO"))
    @NotNull(message = "Processo não pode ser nulo")
    @JsonInclude(Include.NON_NULL)
    private Processo processo;

    @Column(name = "numero_processo")
    private String numeroProcesso;

    public ProcessoRelacionado() {
    }

    public ProcessoRelacionado(
        String numeroProcesso,
        Processo processo
    ) {
        this.processo = processo;
        this.numeroProcesso = numeroProcesso;
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

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }
}
