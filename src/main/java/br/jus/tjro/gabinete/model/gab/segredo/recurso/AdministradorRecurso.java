package br.jus.tjro.gabinete.model.gab.segredo.recurso;

import br.jus.tjro.gabinete.model.gab.processo.Processo;

public class AdministradorRecurso implements Recursos {


    @Override
    public Boolean isLeProcesso(Processo processo) {
        return true;
    }
}
