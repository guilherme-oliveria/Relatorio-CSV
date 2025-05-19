package br.jus.tjro.gabinete.model.gab.transiente;

import br.jus.tjro.gabinete.interfaces.importacao.EntidadeDoisBancos;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.enums.TipoPolo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;


public class Partes implements EntidadeDoisBancos {

    @Id
    @JsonProperty("idParte")
    @JsonInclude(value = Include.NON_NULL)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pessoa")
    @JsonInclude(value = Include.NON_NULL)
    private Pessoa pessoa;

    @Enumerated(EnumType.STRING)
    @JsonProperty("polo")
    @JsonInclude(value = Include.NON_NULL)
    private TipoPolo tipoPolo;

    @JsonInclude(value = Include.NON_NULL)
    private String tipoParte;

    @JsonInclude(value = Include.NON_NULL)
    private String descricaoTipoParte;

    @JsonInclude(value = Include.NON_NULL)
    private String procuradoria;

    @JsonInclude(value = Include.NON_NULL)
    private String descricaoProcuradoria;

    @JsonInclude(value = Include.NON_NULL)
    private String nome;

    @JsonInclude(value = Include.NON_NULL)
    private String tipoDocumento;

    @JsonInclude(value = Include.NON_NULL)
    private String nrDocumento;

    @JsonInclude(value = Include.NON_NULL)
    private String login;

    @JsonInclude(value = Include.NON_NULL)
    private String email;

    @JsonInclude(value = Include.NON_NULL)
    private int idPessoaLegado;

    @JsonInclude(value = Include.NON_NULL)
    private String descricaoPolo;

    @Transient
    private String oab = "";

    @Transient
    private String cpf;

    public Partes() {
    }

    @Deprecated
    public Partes(
        String nome, String login,
        Long idPessoaLegado, String nomePessoa, String emailPessoa,
        TipoPolo tipoPolo, String tipoParte, String procuradoria
    ) {
        this.nome = nome;
        this.nrDocumento = login;
        this.pessoa = new Pessoa(idPessoaLegado, nomePessoa, emailPessoa,null,null,null,null);
        this.tipoPolo = tipoPolo;
        this.tipoParte = tipoParte;
        this.procuradoria = procuradoria;
    }

    public Partes(Long id, String nome, TipoPolo tipoPolo, String tipoParte, String procuradoria) {
        this.id = id;
        this.nome = nome;
        this.descricaoPolo = tipoPolo.getLabel();
        this.tipoParte = tipoParte;
        this.procuradoria = procuradoria;
    }

    public Partes(Long id, String nome, TipoPolo tipoPolo, String tipoParte, String procuradoria, String nrDocumento) {
        this.id = id;
        this.nome = nome;
        this.descricaoPolo = tipoPolo.getLabel();
        this.tipoParte = tipoParte;
        this.procuradoria = procuradoria;
        this.nrDocumento = nrDocumento;
    }

    public Partes(Long id, String nome, TipoPolo tipoPolo, String tipoParte, String procuradoria, String tipoDocumento, String nrDocumento) {
        this.id = id;
        this.nome = nome;
        this.descricaoPolo = tipoPolo.getLabel();
        this.tipoParte = tipoParte;
        this.procuradoria = procuradoria;
        this.tipoDocumento = tipoDocumento;
        if(this.tipoDocumento.equals("OAB")) {
            this.oab = nrDocumento;
        } else {
            this.cpf = nrDocumento;
        }
        this.nrDocumento = nrDocumento;
    }

    public String getDescricaoTipoParte() {
        return tipoParte;
    }

    public void setDescricaoTipoParte(String descricaoTipoParte) {
        this.descricaoTipoParte = descricaoTipoParte;
        this.setTipoParte(descricaoTipoParte);
    }

    public String getDescricaoProcuradoria() {
        return procuradoria;
    }

    public void setDescricaoProcuradoria(String descricaoProcuradoria) {
        this.descricaoProcuradoria = descricaoProcuradoria;
        this.setProcuradoria(descricaoProcuradoria);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNrDocumento() {
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdPessoaLegado() {
        return idPessoaLegado;
    }

    public void setIdPessoaLegado(int idPessoaLegado) {
        this.idPessoaLegado = idPessoaLegado;
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

    public String getDescricaoPolo() {
        return descricaoPolo;
    }

    public void setDescricaoPolo(String descricaoPolo) {
        this.descricaoPolo = descricaoPolo;
    }

    public String getOab() {
        return oab.isEmpty() ? "DESCONHECIDO" : "";
    }

    public void setOab(String v) {
        this.oab = v;
    }

    @Override
    public String getObjetoKeyString() {
        return id.toString();
    }

    @Override
    public String getObjetoUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tipoParte);
        if(idPessoaLegado > 0){
            sb.append(idPessoaLegado);
        }
        return sb.toString();
    }
}
