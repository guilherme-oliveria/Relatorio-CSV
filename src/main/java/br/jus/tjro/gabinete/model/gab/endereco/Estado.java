package br.jus.tjro.gabinete.model.gab.endereco;


import jakarta.persistence.*;


@Entity
@Table(name = "ESTADO")
@SequenceGenerator(name = Estado.SEQUENCE_NAME, sequenceName = Estado.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class Estado {

    static final String SEQUENCE_NAME = "SEQ_ESTADO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private int id;

    @Column(length = 2, unique = true)
    private String uf;

    @Column(unique = true)
    private String descricao;

    public Estado() {
    }

    public Estado(String uf) {
        this.uf = uf;
    }

    public Estado(String uf, String descricao) {
        this.uf = uf;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
