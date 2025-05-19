package br.jus.tjro.gabinete.model.gab.pessoa;

import br.jus.tjro.gabinete.model.gab.endereco.Estado;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;


@Entity
@Table(name = "PESSOA_DOCUMENTO")
@SequenceGenerator(name = PessoaDocumento.SEQUENCE_NAME, sequenceName = PessoaDocumento.SEQUENCE_NAME,
    initialValue = 1, allocationSize = 1)
public class PessoaDocumento {

    static final String SEQUENCE_NAME = "SEQ_PESSOA_DOCUMENTOS";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(name = "id_documento_legado")
    private Long idDocumentoLegado;

    @Column(name = "id_pessoa_legado")
    private Long idPessoaLegado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", foreignKey = @ForeignKey(name = "FK_PESSOA_DOCUMENTO_PESSOA"))
    @JsonBackReference
    private Pessoa pessoa;

    @Column(name = "tipo_documento", length = 3)
    private String tipoDocumento;

    @Column(name = "documento",length = 255)
    private String documento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado", foreignKey = @ForeignKey(name = "FK_PESSOA_DOCUMENTO_ESTADO"))
    private Estado estado;

    @Transient
    private String uf;

    public PessoaDocumento() {
    }

    public PessoaDocumento(Long idDocumentoLegado, Long idPessoaLegado, Pessoa pessoa,
                           String tipoDocumento, String documento, Estado estado) {
        this.idDocumentoLegado = idDocumentoLegado;
        this.idPessoaLegado = idPessoaLegado;
        this.pessoa = pessoa;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.estado = estado;
    }

    public String getUf() {
        if (estado != null)
            return estado.getUf();
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDocumentoLegado() {
        return idDocumentoLegado;
    }

    public void setIdDocumentoLegado(Long idDocumentoLegado) {
        this.idDocumentoLegado = idDocumentoLegado;
    }

    public Long getIdPessoaLegado() {
        return idPessoaLegado;
    }

    public void setIdPessoaLegado(Long idPessoaLegado) {
        this.idPessoaLegado = idPessoaLegado;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
