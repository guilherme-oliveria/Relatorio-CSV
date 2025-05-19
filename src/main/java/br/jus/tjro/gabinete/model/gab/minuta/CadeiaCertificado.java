package br.jus.tjro.gabinete.model.gab.minuta;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "CADEIA_CERTIFICADO")
@SequenceGenerator(name = CadeiaCertificado.SEQUENCE_NAME, sequenceName = CadeiaCertificado.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class CadeiaCertificado implements Serializable {


    public static final String SEQUENCE_NAME = "SEQ_CAD_CERTIFICADO";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @NotNull
    private Long id;

    @NotNull
    @Column(name = "hash_cadeia_certificado", length = 1000)
    private String hashCadeiaCertificado;

    @NotNull
    @Column(name = "cadeia_certificado", columnDefinition="TEXT")
    private String cadeiaCertificado;

    public CadeiaCertificado() {
    }

    public CadeiaCertificado(String hashCadeiaCertificado, String cadeiaCertificado) {
        this.hashCadeiaCertificado = hashCadeiaCertificado;
        this.cadeiaCertificado = cadeiaCertificado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashCadeiaCertificado() {
        return hashCadeiaCertificado;
    }

    public void setHashCadeiaCertificado(String hashCadeiaCertificado) {
        this.hashCadeiaCertificado = hashCadeiaCertificado;
    }

    public String getCadeiaCertificado() {
        return cadeiaCertificado;
    }

    public void setCadeiaCertificado(String cadeiaCertificado) {
        this.cadeiaCertificado = cadeiaCertificado;
    }


}
