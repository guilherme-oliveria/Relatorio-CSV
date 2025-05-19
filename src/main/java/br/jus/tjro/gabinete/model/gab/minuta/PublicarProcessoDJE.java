package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.transiente.ProcessoConcluso;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "PUBLICAR_DJE")
@SequenceGenerator(name = PublicarProcessoDJE.SEQUENCE_NAME, sequenceName = PublicarProcessoDJE.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class PublicarProcessoDJE {

    public static final String SEQUENCE_NAME = "SEQUENCIA_PUBLICAR_DJE";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Long id;

    @JsonInclude(Include.NON_NULL)
    @Column(name = "PRAZO")
    @NotNull
    private Long prazo;

    @Column(name = "CONTEUDO")
    @JsonInclude(Include.NON_NULL)
    private String conteudo;

    @OneToOne
    @JoinColumn(name = "ID_MINUTA", foreignKey = @ForeignKey(name = "FK_PUBLICAR_DJE_MINUTA"))
    @JsonBackReference
    private Minuta minuta;

    private Boolean publicar;

    public PublicarProcessoDJE() {
        publicar = false;
        prazo = 0l;
    }


    public PublicarProcessoDJE( ProcessoConcluso processoConcluso) {
    }

    @JsonCreator
    public PublicarProcessoDJE(@JsonProperty(value = "id")
                               Long id,
                               @JsonProperty("prazo") Long prazo,
                               @JsonProperty("conteudo") String conteudo,
                               @JsonProperty("minuta") Minuta minuta,
                               @JsonProperty(value = "publicar",defaultValue = "false") boolean publicar) throws Exception {
        this.id = id;
        if(prazo == null)
            this.prazo = 0l;
        else
            this.prazo = prazo;

        this.conteudo = conteudo;
        this.minuta = minuta;
        this.publicar = publicar;
        if(this.publicar && this.prazo <= 0)
            throw new Exception("Para publicar no DJE o prazo deve ser maior que zero");
    }

    public Boolean getPublicar() {
        return publicar != null && publicar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrazo() {
        return prazo;
    }

    public void setPrazo(Long prazo) {
        this.prazo = prazo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Minuta getMinuta() {
        return minuta;
    }

    public void setMinuta(Minuta minuta) {
        this.minuta = minuta;
    }

    public void setPublicar(Boolean publicar) {
        this.publicar = publicar;
    }

    public void copiaPropriedades(PublicarProcessoDJE publicacaoDje) {
        setPrazo(publicacaoDje.getPrazo());
        setConteudo(publicacaoDje.getConteudo());
        setMinuta(publicacaoDje.getMinuta());
        setPublicar(publicacaoDje.getPublicar());
    }
}
