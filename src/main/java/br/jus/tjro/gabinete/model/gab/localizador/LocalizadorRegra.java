package br.jus.tjro.gabinete.model.gab.localizador;

import org.hibernate.annotations.Type;

import jakarta.persistence.*;

@Entity
@Table(name = "LOC_REGRA")
@SequenceGenerator(name = LocalizadorRegra.SEQUENCE_NAME, sequenceName = LocalizadorRegra.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class LocalizadorRegra {

    public static final String SEQUENCE_NAME = "SEQUENCIA_LOC_REGRA";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PARAMETRO")
    private String parametro;


    public LocalizadorRegra() {
    }

    public LocalizadorRegra(Long id) {
        this.id = id;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
