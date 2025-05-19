package br.jus.tjro.gabinete.model.gab.processo;

import br.jus.tjro.gabinete.interfaces.importacao.EntidadeDoisBancos;
import br.jus.tjro.gabinete.model.gab.enums.TipoPessoaEnum;
import br.jus.tjro.gabinete.model.gab.enums.TipoPolo;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.vavr.collection.HashMap;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "PROCESSO_PARTE")
@SequenceGenerator(name = ProcessoParte.SEQUENCE_NAME, sequenceName = ProcessoParte.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class ProcessoParte implements EntidadeDoisBancos {

    public static final String SEQUENCE_NAME = "SEQUENCIA_PROCESSO_PARTE";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @NotNull
    @Column(name = "id_parte_legado", unique = true)
    private Long idParteLegado;

    @Column(name = "tipo_parte", length = 255)
    private String tipoParte;

    @Column(name = "procuradoria", length = 255)
    private String procuradoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa", foreignKey = @ForeignKey(name = "FK_PROCESSO_PARTE_PESSOA"))
    @NotNull
    private Pessoa pessoa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_polo")
    private TipoPolo tipoPolo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_processo", foreignKey = @ForeignKey(name = "FK_PROCESSO_PARTE_PROCESSO"))
    @JsonIgnore
    private Processo processo;

    @JsonManagedReference(value = "parte-enderecos")
    @OneToMany(mappedBy = "processoParte")
    private List<ProcessoParteEndereco> processoParteEnderecos;

    @Transient
    private String nome;

    @Transient
    private String login;

    @Transient
    private Long idPessoaLegado;

    @Transient
    private Boolean sobSegredo = false;

    @Transient
    private String oab;

    @Transient
    private Boolean isProcuradoria;

    public ProcessoParte() {
    }

    public ProcessoParte(Long id) {
        this.id = id;
    }

    public ProcessoParte(Long idParteLegado, String tipoParte, String procuradoria, TipoPolo tipoPolo,
                         Processo processo) {
        this.idParteLegado = idParteLegado;
        this.tipoParte = tipoParte;
        this.procuradoria = procuradoria;
        this.tipoPolo = tipoPolo;
        this.processo = processo;
    }

    public ProcessoParte(Long idParteLegado, String tipoParte, String procuradoria, TipoPolo tipoPolo, Processo processo, Pessoa pessoa) {
        this.idParteLegado = idParteLegado;
        this.tipoParte = tipoParte;
        this.procuradoria = procuradoria;
        this.tipoPolo = tipoPolo;
        this.processo = processo;
        this.pessoa = pessoa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdParteLegado() {
        return idParteLegado;
    }

    public void setIdParteLegado(Long idParteLegado) {
        this.idParteLegado = idParteLegado;
    }

    public String getTipoParte() {
        return tipoParte;
    }

    public void setTipoParte(String tipoParte) {
        this.tipoParte = tipoParte;
    }

    public String getProcuradoria() {
        return procuradoria;
    }

    public void setProcuradoria(String procuradoria) {
        this.procuradoria = procuradoria;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public TipoPolo getTipoPolo() {
        return tipoPolo;
    }

    public void setTipoPolo(TipoPolo tipoPolo) {
        this.tipoPolo = tipoPolo;
    }

    public String getNome() {
        if (this.pessoa == null || this.pessoa.getNome() == null || this.pessoa.getNome().isEmpty())
            return "";
        return this.pessoa.getNome();
    }

    @Deprecated
    public void setNome(String nome) {
        this.pessoa.setNome(nome);
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    public List<ProcessoParteEndereco> getProcessoParteEnderecos() {
        return processoParteEnderecos == null ? new ArrayList<>() : processoParteEnderecos;
    }

    public void setProcessoParteEnderecos(List<ProcessoParteEndereco> processoParteEnderecos) {
        this.processoParteEnderecos = processoParteEnderecos;
    }

    //TODO arrumar a condicao para ficar mais legil e remover o estado
    public Boolean getSobSegredo() {
        if (!this.getTipoParte().equals("ADVOGADO")) {
            this.setSobSegredo(processo.isSegredoJustica() || TipoPessoaEnum.A.equals(this.getPessoa().getTipoPessoa()));
        }
        return sobSegredo;
    }

    public void setSobSegredo(Boolean sobSegredo) {
        this.sobSegredo = sobSegredo;
    }

    public Long getIdPessoaLegado() {
        return idPessoaLegado;
    }

    public void setIdPessoaLegado(Long idPessoaLegado) {
        this.idPessoaLegado = idPessoaLegado;
    }

    public Boolean isProcuradoria() {
        return this.getProcuradoria() != null && !this.getProcuradoria().isEmpty();
    }

    @Override
    public String getObjetoKeyString() {
        return idParteLegado.toString();
    }

    @Override
    public String getObjetoUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tipoParte);
        if(pessoa != null && pessoa.getIdPessoaLegado() != null) {
            sb.append(pessoa.getIdPessoaLegado());
        }
        return sb.toString();
    }

    public HashMap<String,String> getHashMapModelo(){
        return HashMap.of(
            "nome",nome,
            "tipo_parte",tipoParte,
            "tipo_pessoa",pessoa.getTipoPessoa().toString(),
            "endereco", endereco(),
            "polo",tipoPolo.getLabel(),
            "procuradoria" , procuradoria == null ? "" : procuradoria,
            "documento" , documento()
            );
    }

    private String endereco() {
        if(processoParteEnderecos.isEmpty())
            return "";
        Optional<ProcessoParteEndereco> endereco = processoParteEnderecos.stream().findFirst();
        return endereco.isPresent() ? endereco.get().getEndereco().getEnderecoCompleto() : "";
    }

    private String documento() {
        var documento = "";
        var individuo = pessoa;
        if (individuo == null) return "";
        if (isProcuradoria()) {
            documento = "";
        } else if (getTipoParte().equals("ADVOGADO")) {
            documento = individuo.getDocumentoPorTipo("OAB");
        } else if (individuo.getTipoPessoa().equals(TipoPessoaEnum.F)) {
            documento = individuo.getDocumentoPorTipo("CPF");
        } else if (individuo.getTipoPessoa().equals(TipoPessoaEnum.J)) {
            documento = individuo.getDocumentoPorTipo("CPJ");
        }
        return documento;
    }
}
