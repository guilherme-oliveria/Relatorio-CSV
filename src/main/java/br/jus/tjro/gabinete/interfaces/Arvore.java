package br.jus.tjro.gabinete.interfaces;

import java.util.List;

public interface Arvore<T extends Arvore<T>> {

    Long getId();

    void setId(Long id);

    Long getIdPai();

    void setIdPai(Long idPai);

    List<T> getFilhos();

    void setFilhos(List<T> filhos);

}
