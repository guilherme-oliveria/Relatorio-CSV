package br.jus.tjro.gabinete.interfaces;

import java.util.List;


public interface TpuInterface {

    Long getCodigo();

    void setCodigo(Long codigo);

    Boolean getTemFilhos();

    void setTemFilhos(Boolean temFilhos);

    String getDescricao();

    void setDescricao(String descricao);

    String getGlossario();

    void setGlossario(String glossario);

    String getResourceName();

    void setResourceName(String rn);

    List<TpuInterface> getFilhos();

    void setFilhos(List<TpuInterface> filhos);

    Long getCodigoPai();

    void setCodigoPai(Long codigoPai);

    List<Long> getBreadcrumb();

    void setBreadcrumb(List<Long> breadcrum);

    String getSituacao();

    void setSituacao(String situacao);


    void addFilho(TpuInterface filho);
}
