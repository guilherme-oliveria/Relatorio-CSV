package br.jus.tjro.gabinete.model.gab.minuta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ASSINATURA")
@SequenceGenerator(name = Assinatura.SEQUENCE_NAME, sequenceName = Assinatura.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class Assinatura implements Serializable {

    public static final String SEQUENCE_NAME = "SEQUENCIA_ASSINATURA";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @NotNull
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_cadeia_certificado", foreignKey = @ForeignKey(name = "FK_ASSINATURA_CERTIFICADO"))
    private CadeiaCertificado cadeiaCertificado;

    @NotNull
    @Column(name = "assinatura", columnDefinition="TEXT")
    private String assinatura;

    @NotNull
    @Column(name = "algoritmo_digest")
    private String algoritmoDigest;

    @NotNull
    @Column(name = "cpf_usuario", length = 14)
    private String cpfUsuario;

    @NotNull
    @Column(name = "data_assinatura")
    private Date dataAssinatura;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }

    public String getAlgoritmoDigest() {
        return algoritmoDigest;
    }

    public void setAlgoritmoDigest(String algoritmoDigest) {
        this.algoritmoDigest = algoritmoDigest;
    }

    public String getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public Date getDataAssinatura() {
        return dataAssinatura;
    }

    public void setDataAssinatura(Date dataAssinatura) {
        this.dataAssinatura = dataAssinatura;
    }

    public CadeiaCertificado getCadeiaCertificado() {
        return cadeiaCertificado;
    }

    public void setCadeiaCertificado(CadeiaCertificado cadeiaCertificado) {
        this.cadeiaCertificado = cadeiaCertificado;
    }
}
