package br.jus.tjro.gabinete.model.gab.tpu;

import br.jus.tjro.gabinete.interfaces.TpuInterface;
import br.jus.tjro.gabinete.util.TpuDeserializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonDeserialize(using = TpuDeserializer.class)
public abstract class Tpu implements TpuInterface, Cloneable {

    protected String resourceName;
    private Long codigo;
    private String descricao;
    private String glossario;
    //@JsonDeserialize(using = TpuArvoreDeserializer.class)
    private List<TpuInterface> filhos = new ArrayList<>();
    private Long codigoPai;
    private List<Long> breadcrumb = new ArrayList<>();
    private String situacao;
    private Boolean temFilhos;
    
    @JsonIgnore
    private final Logger looger = LoggerFactory.getLogger(Tpu.class);

    public Tpu(Long codigo) {
        this.codigo = codigo;
    }

    public Tpu(Long codigo, String descricao, String glossario, Long codigoPai, String situacao, Boolean temFilhos) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.glossario = glossario;
        this.codigoPai = codigoPai;
        this.situacao = situacao;
        this.temFilhos = temFilhos;
    }

    public Tpu() {
    }

    @Override
    public Long getCodigo() {
        return codigo;
    }

    @Override
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    @Override
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String getGlossario() {
        return glossario;
    }

    @Override
    public void setGlossario(String glossario) {
        this.glossario = glossario;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    @Override
    public void setResourceName(String rn) {
        this.resourceName = rn;
    }

    @Override
    public List<TpuInterface> getFilhos() {
        return filhos;
    }

    @Override
    public void setFilhos(List<TpuInterface> filhos) {
        this.filhos = filhos;
    }

    @Override
    public void addFilho(TpuInterface filho) {
        this.filhos.add(filho);
    }

    @Override
    public Long getCodigoPai() {
        return codigoPai;
    }

    @Override
    public void setCodigoPai(Long codigoPai) {
        this.codigoPai = codigoPai;
    }

    @Override
    public List<Long> getBreadcrumb() {
        return breadcrumb;
    }

    @Override
    public void setBreadcrumb(List<Long> breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    @Override
    public String getSituacao() {
        return situacao;
    }

    @Override
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public boolean ativo() {
        return getSituacao().equals("A");
    }

    public TpuInterface clonar() {
        try {
            return (TpuInterface) this.clone();
        } catch (CloneNotSupportedException e) {
            looger.error(e.getMessage(),e);
            return null;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tpu other = (Tpu) obj;
        if (codigo == null) {
            return other.codigo == null;
        } else return codigo.equals(other.codigo);
    }

    @Override
    public Boolean getTemFilhos() {
        return temFilhos;
    }

    @Override
    public void setTemFilhos(Boolean temFilhos) {
        this.temFilhos = temFilhos;
    }
}
