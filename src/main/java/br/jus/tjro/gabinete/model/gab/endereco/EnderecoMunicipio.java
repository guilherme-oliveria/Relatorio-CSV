package br.jus.tjro.gabinete.model.gab.endereco;


import jakarta.persistence.*;

@Entity
@Table(
    name = "ENDERECO_MUNICIPIO",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"descricao", "id_estado"}, name = "UK_ENDERECO_MUNICIPIO_ID_ESTAD")
    }
)
@SequenceGenerator(name = EnderecoMunicipio.SEQUENCE_NAME, sequenceName = EnderecoMunicipio.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class EnderecoMunicipio {

    static final String SEQUENCE_NAME = "SEQ_ENDERECO_MUNICIPIO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private int id;

    @Column(length = 50)
    private String descricao;

    @OneToOne
    @JoinColumn(name = "id_estado", foreignKey = @ForeignKey(name = "FK_ENDERECO_MUNICIPIO_ESTADO"))
    private Estado estado;

    public EnderecoMunicipio() {
    }

    public EnderecoMunicipio(String descricao, Estado estado) {
        this.descricao = descricao;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
