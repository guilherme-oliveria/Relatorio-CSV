package br.jus.tjro.gabinete.model.gab.minuta;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "MINUTA_MOVIMENTO")
@SequenceGenerator(name = MinutaMovimento.SEQUENCE_NAME, sequenceName = MinutaMovimento.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class MinutaMovimento {

    @Transient
    public static final String SEQUENCE_NAME = "SEQUENCIA_MINUTA_MOVIMENTO";

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "minuta_id", foreignKey = @ForeignKey(name = "FK_MINUTA_MOVIMENTO_MINUTA"))
    @JsonBackReference
    private Minuta minuta;

    @Column(name = "movimento_id")
    private Long movimentoId;

    @Column(name = "ordem")
    private Integer ordem;

    @Transient
    private String descricao;

    @OneToMany(mappedBy = "minutaMovimento", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonInclude(Include.NON_NULL)
    private List<MinutaMovimentoComplemento> minutaMovimentoComplementos;

    public MinutaMovimento() { }

    public MinutaMovimento(Minuta minuta, Long movimentoId) {
        this.minuta = minuta;
        this.movimentoId = movimentoId;
        this.minutaMovimentoComplementos = minutaMovimentoComplementos;
    }

    public MinutaMovimento(Long idMinuta, Long movimentoId) {
        this.minuta = new Minuta(idMinuta);
        this.movimentoId = movimentoId;
        minuta = new Minuta(idMinuta);
    }

    @Transient
    public static void ordenaPorOrdem(List<MinutaMovimento> minutaMovimentos) {
        if(minutaMovimentos != null)
            Collections.sort(minutaMovimentos, (o1, o2) -> {
                if (o1.getOrdem().equals(o2.getOrdem()))
                    return o1.getId().compareTo(o2.getId());
                return o1.getOrdem().compareTo(o2.getOrdem());
            });
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

    public Long getMovimentoId() {
        return movimentoId;
    }

    public void setMovimentoId(Long movimentoId) {
        this.movimentoId = movimentoId;
    }

    public List<MinutaMovimentoComplemento> getMinutaMovimentoComplementos() {
        return minutaMovimentoComplementos;
    }

    public void setMinutaMovimentoComplementos(List<MinutaMovimentoComplemento> minutaMovimentoComplementos) {
        this.minutaMovimentoComplementos = minutaMovimentoComplementos;
    }

    @Transient
    public Integer getOrdem() {
        if (this.ordem == null) {
            return 0;
        }
        return this.ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
