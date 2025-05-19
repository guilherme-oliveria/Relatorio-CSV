package br.jus.tjro.gabinete.model.gab.minuta.modelos;

import br.jus.tjro.gabinete.model.docs.Tag;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Optional.ofNullable;

public class ModeloMinuta implements Cloneable {

    public ModeloMinuta(
        @JsonProperty(value = "descricao",defaultValue = "Sem Descrição") String descricao,
        @JsonProperty("template") String template,
        @JsonProperty("idOrgaoJulgador") String idOrgaoJulgador,
        @JsonProperty("idTipoDocumento") String idTipoDocumento,
        @JsonProperty(value = "tags", required = false) List<Tag> tags) {
        this.descricao = descricao;
        this.template = template;
        this.idOrgaoJulgador = idOrgaoJulgador;
        this.idTipoDocumento = idTipoDocumento;
        this.tags = ofNullable(tags).orElse(new ArrayList<>());
    }

    public Boolean meu = false;

    public Boolean vara = false;

    public Boolean editar = false;

    public Boolean excluir = false;

    private String id;

    private final String descricao;

    private final String template;

    private final String sistema = "gabinete";

    private final String idOrgaoJulgador;

    private final List<Tag> tags;

    private String cpf;

    private TipoDocumento tipoDocumento;

    private String idTipoDocumento;

    private String cpfAtualizacao;

    private String cpfExclusao;

    private Date dataExclusao;

    private Date atualizacao = new Date();

    public ModeloMinuta clonar() {
        try {
            return (ModeloMinuta) this.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTemplate() {
        return template;
    }

    public String getCpf() {
        return cpf;
    }

    public String getIdOrgaoJulgador() {
        return idOrgaoJulgador;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        if(tipoDocumento != null)
            this.idTipoDocumento = tipoDocumento.getId();
        this.tipoDocumento = tipoDocumento;
    }

    public String getCpfAtualizacao() {
        return cpfAtualizacao;
    }

    public void setCpfAtualizacao(String cpfAtualizacao) {
        this.cpfAtualizacao = cpfAtualizacao;
    }

    public Date getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(Date dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public String getCpfExclusao() {
        return cpfExclusao;
    }

    public void setCpfExclusao(String cpfExclusao) {
        this.cpfExclusao = cpfExclusao;
    }

    public Date getAtualizacao() {
        return atualizacao;
    }

    public void setAtualizacao(Date atualizacao) {
        this.atualizacao = atualizacao;
    }

    public String getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(String idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getSistema() {
        return sistema;
    }

    @JsonIgnore
    public boolean isCanEditOrDelete(Usuario usuario){
        boolean retorno = usuario != null && usuario.getCpf() != null && usuario.getCpf().equals(cpf);
        if(usuario != null && usuario.getOrgaosJulgadores() != null && usuario.getOrgaosJulgadores().contains(idOrgaoJulgador))
            retorno = true;
        vara = retorno;
        meu = retorno;
        editar = retorno;
        excluir = retorno;
        return retorno;
    }

    public void setUsuarioSaveUpdate(Usuario usuario) {
        if(usuario != null && usuario.getCpf() != null) {
            if (cpf == null)
                cpf = usuario.getCpf();
            cpfAtualizacao = usuario.getCpf();
        }
    }

    public List<Tag> getTags() {
        return tags;
    }
}
