package br.jus.tjro.gabinete.model.gab.pessoa;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.EntidadeDoisBancos;
import br.jus.tjro.gabinete.model.gab.enums.TipoPessoaEnum;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "pessoa")
@SequenceGenerator(name = Pessoa.SEQUENCE_NAME, sequenceName = Pessoa.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pessoa implements EntidadeDoisBancos {

    static final String SEQUENCE_NAME = "SEQUENCIA_PESSOA";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "id_pessoa_legado", unique = true)
    private Long idPessoaLegado;

    @Column
    private String nome;

    @Column
    private String email;

    @Column(name = "data_nascimento")
    private Date dataNascimento;

    @Column(name = "data_obito")
    private Date dataObito;

    @Column(name = "SISTEMA")
    @Enumerated(EnumType.STRING)
    private FonteDadosEnum sistema;

    @JsonProperty("in_tipo_pessoa")
    @Column(name = "in_tipo_pessoa")
    @Enumerated(EnumType.STRING)
    private TipoPessoaEnum tipoPessoa;

    @JsonManagedReference
    @OneToMany(mappedBy = "pessoa", fetch = FetchType.EAGER)
    private List<PessoaDocumento> pessoaDocumentos = new ArrayList<>();

    public Pessoa() {
    }

    public Pessoa(Long idPessoaLegado, String nome, String email, Date dataNascimento,
                  Date dataObito, TipoPessoaEnum tipoPessoa, FonteDadosEnum sistema) {
        this.idPessoaLegado = idPessoaLegado;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.dataObito = dataObito;
        this.tipoPessoa = tipoPessoa;
        this.sistema = sistema;
    }

    public Pessoa(Long idPessoa) {
        this.id = idPessoa;
    }

    public String getDocumentoPorTipo(String tipo) {
        if (this.getPessoaDocumentos() == null) return "DESCONHECIDO";
        return this.getPessoaDocumentos().stream()
            .filter(d -> d.getDocumento() != null && d.getTipoDocumento() != null && d.getTipoDocumento().equals(tipo))
            .map(PessoaDocumento::getDocumento).findFirst()
            .orElse("DESCONHECIDO");
    }

    @Override
    public String getObjetoKeyString() {
        if(idPessoaLegado != null)
            return idPessoaLegado.toString();
        else
            return null;
    }

    @Override
    public String getObjetoUpdateString() {
        return this.nome;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPessoaLegado() {
        return idPessoaLegado;
    }

    public void setIdPessoaLegado(Long idPessoaLegado) {
        this.idPessoaLegado = idPessoaLegado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Date getDataObito() {
        return dataObito;
    }

    public void setDataObito(Date dataObito) {
        this.dataObito = dataObito;
    }

    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public List<PessoaDocumento> getPessoaDocumentos() {
        return pessoaDocumentos;
    }

    public void setPessoaDocumentos(List<PessoaDocumento> pessoaDocumentos) {
        this.pessoaDocumentos = pessoaDocumentos;
    }

    public FonteDadosEnum getSistema() {
        return sistema;
    }

    public void setSistema(FonteDadosEnum sistema) {
        this.sistema = sistema;
    }
}
