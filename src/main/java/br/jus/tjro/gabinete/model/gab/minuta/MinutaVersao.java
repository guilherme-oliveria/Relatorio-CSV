package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.model.gab.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Entity
@Table(name = "minuta_versao")
@SequenceGenerator(name = MinutaVersao.SEQUENCE_NAME, sequenceName = MinutaVersao.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MinutaVersao implements Serializable {

    public static final String SEQUENCE_NAME = "SEQUENCIA_MINUTA_VERSAO";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "minuta_html", columnDefinition="TEXT")
    @JsonInclude(Include.NON_NULL)
    private String minutaHtml;

    @ManyToOne
    @JoinColumn(name = "id_minuta", foreignKey = @ForeignKey(name = "FK_MINUTA_VERSAO_MINUTA"))
    @JsonIgnore
    private Minuta minuta;

    @Column(name = "data_versao")
    private Date dataVersao;

    @Column(name = "autor_versao")
    private String autorVersao;

    @Column(name = "versao")
    private Integer versao;

    @Column(name = "nome_autor")
    private String nomeAutor;

    public MinutaVersao() {    }

    public MinutaVersao(Minuta minuta, Integer versao, Usuario usuario) {
        this.minuta = minuta;
        this.minutaHtml = minuta.getMinutaHtml();
        this.dataVersao = new Date();
        this.versao = versao;
        if (minuta.getAutor() != null)
            this.autorVersao = usuario.getCpf();
        this.nomeAutor = minuta.getNomeAutor();
    }

    public MinutaVersao(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Minuta getMinuta() {
        return minuta;
    }

    public void setMinuta(Minuta minuta) {
        this.minuta = minuta;
    }

    @JsonInclude(Include.NON_NULL)
    public Long getIdMinuta() {
        return minuta.getId();
    }

    public void setIdMinuta(Long idMinuta) {
        this.minuta = new Minuta(idMinuta);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        Minuta doc = (Minuta) obj;
        return doc.getId().equals(id);
    }

    @JsonIgnore
    public InputStream getMinutaHtmlInputStream() throws UnsupportedEncodingException {
        ByteArrayInputStream retorno = new ByteArrayInputStream(this.minutaHtml.getBytes(StandardCharsets.UTF_8.name()));
        return retorno;
    }

    @JsonIgnore
    public byte[] getMinutaHtmlBytes() throws UnsupportedEncodingException {
        return this.minutaHtml.getBytes(StandardCharsets.UTF_8.name());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMinutaHtml() {
        return minutaHtml;
    }

    public void setMinutaHtml(String minutaHtml) {
        this.minutaHtml = minutaHtml;
    }

    public Date getDataVersao() {
        return dataVersao;
    }

    public void setDataVersao(Date dataVersao) {
        this.dataVersao = dataVersao;
    }

    public String getAutorVersao() {
        return autorVersao;
    }

    public void setAutorVersao(String autorVersao) {
        this.autorVersao = autorVersao;
    }

    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }
}
