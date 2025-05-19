package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.model.gab.endereco.Endereco;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;


@Entity
@Table(name = "PROCESSO_PARTE_ENDERECO")
@SequenceGenerator(name = ProcessoParteEndereco.SEQUENCE_NAME, sequenceName = ProcessoParteEndereco.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class ProcessoParteEndereco {

    static final String SEQUENCE_NAME = "SEQ_PROC_PARTE_ENDERECO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_processo_parte", foreignKey = @ForeignKey(name = "FK_PROC_PARTE_ENDERECO_PRO_PAR"))
    @JsonBackReference(value = "parte-enderecos")
    private ProcessoParte processoParte;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_endereco", foreignKey = @ForeignKey(name = "FK_PROC_PARTE_ENDERECO_END"))
    private Endereco endereco;

    @Column(name = "id_processo_parte_legado")
    private Long idProcessoParte;

    @Column(name = "id_endereco_legado")
    private Long idEnderecoLegado;

    public ProcessoParteEndereco() {
    }

    public ProcessoParteEndereco(ProcessoParte processoParte, Endereco endereco, Long idProcessoParteLegado, Long idEnderecoLegado) {
        this.processoParte = processoParte;
        this.endereco = endereco;
        this.idProcessoParte = idProcessoParteLegado;
        this.idEnderecoLegado = idEnderecoLegado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessoParte getProcessoParte() {
        return processoParte;
    }

    public void setProcessoParte(ProcessoParte processoParte) {
        this.processoParte = processoParte;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Long getIdProcessoParte() {
        return idProcessoParte;
    }

    public void setIdProcessoParte(Long idProcessoParte) {
        this.idProcessoParte = idProcessoParte;
    }

    public Long getIdEnderecoLegado() {
        return idEnderecoLegado;
    }

    public void setIdEnderecoLegado(Long idEnderecoLegado) {
        this.idEnderecoLegado = idEnderecoLegado;
    }
}

