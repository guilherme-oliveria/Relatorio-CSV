package br.jus.tjro.gabinete.model.gab.processo;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "PROCURADORIA")
@SequenceGenerator(name = Procuradoria.SEQUENCE_NAME, sequenceName = Procuradoria.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class Procuradoria {
    static final String SEQUENCE_NAME = "SEQ_PROCURADORIA";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "id_legado")
    @NotNull(message = "id_legado não pode ser nulo")
    private Long idLegado;

    @Column(name = "descricao")
    private String descricao;

    public Procuradoria setPropriedadesRemota(Procuradoria remoto) {
        this.descricao = remoto.getDescricao();
        this.idLegado = remoto.getIdLegado();
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdLegado() {
        return idLegado;
    }

    public void setIdLegado(Long idLegado) {
        this.idLegado = idLegado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
