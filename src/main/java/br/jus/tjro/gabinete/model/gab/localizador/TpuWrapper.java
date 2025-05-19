package br.jus.tjro.gabinete.model.gab.localizador;


import java.util.ArrayList;
import java.util.List;

public class TpuWrapper {

    private String codigo;
    private String descricao;
    private String expandido;
    private TpuWrapper tpu;
    private List<Long> breadcrumb = new ArrayList<>();

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getExpandido() {
        return expandido;
    }

    public void setExpandido(String expandido) {
        this.expandido = expandido;
    }

    public TpuWrapper getTpu() {
        return tpu;
    }

    public void setTpu(TpuWrapper tpu) {
        this.tpu = tpu;
    }

    public List<Long> getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(List<Long> breadcrumb) {
        this.breadcrumb = breadcrumb;
    }
}
