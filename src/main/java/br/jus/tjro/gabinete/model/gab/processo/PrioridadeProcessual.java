package br.jus.tjro.gabinete.model.gab.processo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.util.Objects;

@Entity
@Table(name = "PRIORIDADE_PROCESSUAL")
//@SequenceGenerator(name = PrioridadeProcessual.SEQUENCE_NAME, sequenceName = PrioridadeProcessual.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class PrioridadeProcessual {

//    public static final String SEQUENCE_NAME = "SEQUENCIA_PRI_PROCESSUAL";

    @Id
    @Generated(GenerationTime.NEVER)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DESCRICAO", length = 64)
    private String descricao;

    @Column(name = "IDADE_MINIMA")
    private int idadeMinima;

    @Column(name = "ICONE_FONT_AWESOME", length = 64)
    private String fontAwesome;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdadeMinima() {
        return idadeMinima;
    }

    public void setIdadeMinima(int idadeMinima) {
        this.idadeMinima = idadeMinima;
    }

    public String getFontAwesome() {
        return this.fontAwesome;
    }

    public void setFontAwesome(String fontAwesome) {
        this.fontAwesome = fontAwesome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrioridadeProcessual that = (PrioridadeProcessual) o;
        return getIdadeMinima() == that.getIdadeMinima() &&
            Objects.equals(getId(), that.getId()) &&
            Objects.equals(getDescricao(), that.getDescricao()) &&
            Objects.equals(getFontAwesome(), that.getFontAwesome());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getDescricao(), getIdadeMinima(), getFontAwesome());
    }
}
