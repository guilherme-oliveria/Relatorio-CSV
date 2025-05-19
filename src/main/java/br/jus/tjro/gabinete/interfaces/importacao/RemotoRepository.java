package br.jus.tjro.gabinete.interfaces.importacao;


import br.jus.tjro.gabinete.model.gab.processo.Processo;

import java.util.List;

public interface RemotoRepository<E> {

    E findOne(String idLegado, FonteDados fonteDados) throws Exception;

    E[] findAllForProcesso(Processo processo, FonteDados fonteDados) throws Exception;
}
