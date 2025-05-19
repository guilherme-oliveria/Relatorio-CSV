package br.jus.tjro.gabinete.model.gab.question;

import br.jus.tjro.gabinete.model.gab.processo.Processo;

public interface Rule {

    boolean activeBy(Processo processo, QuestionBase questionBase);

}
