package br.jus.tjro.gabinete.model.gab.tag;

import br.jus.tjro.gabinete.interfaces.importacao.EntidadeDoisBancos;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Where;

import java.util.Date;

@Entity
@Table(name = Tag.NomeTabela,
    uniqueConstraints = { @UniqueConstraint(columnNames = {Tag.NomeColunaTAG, Tag.NomeColunaOrgaoJulgador, Tag.NomeColunaDataExclusao}, name = "UK_TAG")},
    indexes = {    @Index(columnList = Tag.NomeColunaOrgaoJulgador, name = "idx_orgao_julgador")    }
    )
@SequenceGenerator(name = Tag.SEQUENCE_NAME, sequenceName = Tag.SEQUENCE_NAME, allocationSize = 1)
@Where(clause = Tag.NomeColunaDataExclusao + " is null")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag implements EntidadeDoisBancos {

    public static final String NomeColunaDataExclusao = "data_exclusao";
    public static final String SEQUENCE_NAME = "SEQUENCIA_TAG";
    public static final String NomeColunaTAG = "tag";
    public static final String NomeColunaOrgaoJulgador = "orgao_julgador_str";
    public static final String NomeTabela = "TAG";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @NotNull
    @Column(name = NomeColunaTAG)
    private String tag;

    @Column(name = NomeColunaOrgaoJulgador)
    private String orgaoJulgador;

    @Column(name = "cor_hexadecimal")
    private String CorHexadecimal;

    @Column(name = NomeColunaDataExclusao)
    private Date dataExclusao;

    @Column(name = "usuario_exclusao")
    private String UsuarioExclusao;

    @Column(name = "id_tag_pje")
    private Integer idTagPje;

    @Column(name = "url")
    private String url;
    @Column(name = "aviso")
    private String aviso;
    @Column(name = "apenas_meu_gabinete")
    private Boolean apenasMeuGabinete;

    public Tag(){}

    public Tag(Long id){
        this.id = id;
    }

    public Tag(String tag, String corHexadecimal, String url, String aviso, Boolean apenasMeuGabinete) {
        this.tag = tag;
        this.CorHexadecimal = corHexadecimal;
        this.url = url;
        this.aviso = aviso;
        this.apenasMeuGabinete = apenasMeuGabinete;
    }

    public Tag(String nome, String cor, String oj) {
        this.tag = nome;
        this.CorHexadecimal = cor;
        this.orgaoJulgador = oj;
    }
    public Tag(String nome, String cor, String oj, Integer idTagPje) {
        this.tag = nome;
        this.CorHexadecimal = cor;
        this.orgaoJulgador = oj;
        this.idTagPje = idTagPje;
    }

    public static final String OrgaoTagTipoSistema = "SISTEMA";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOrgaoJulgador() {
        return orgaoJulgador;
    }

    public void setOrgaoJulgador(String orgaoJulgador) {
        this.orgaoJulgador = orgaoJulgador;
    }

    public String getCorHexadecimal() {
        return CorHexadecimal;
    }

    public void setCorHexadecimal(String corHexadecimal) {
        CorHexadecimal = corHexadecimal;
    }

    public Date getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(Date dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public String getUsuarioExclusao() {
        return UsuarioExclusao;
    }

    public void setUsuarioExclusao(String usuarioExclusao) {
        UsuarioExclusao = usuarioExclusao;
    }

    public boolean isDeSistema() {
        return Tag.OrgaoTagTipoSistema.equals(this.orgaoJulgador);
    }

    public boolean validaApenasMeuGabinete(){
        if(this.getApenasMeuGabinete()!=null){
            return this.getApenasMeuGabinete();
        }else{
            return false;
        }
    }
    @Override
    public String getObjetoKeyString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getIdTagPje());
        return sb.toString();
    }

    @Override
    public String getObjetoUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTag());
        return sb.toString();
    }

    public Integer getIdTagPje() {
        return idTagPje;
    }

    public void setIdTagPje(Integer idTagPje) {
        this.idTagPje = idTagPje;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAviso() {
        return aviso;
    }

    public void setAviso(String aviso) {
        this.aviso = aviso;
    }

    public Boolean getApenasMeuGabinete() {
        return apenasMeuGabinete;
    }

    public void setApenasMeuGabinete(Boolean apenasMeuGabinete) {
        this.apenasMeuGabinete = apenasMeuGabinete;
    }
}
