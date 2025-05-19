package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.enums.TipoPolo;


public class ProcessoParteTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ProcessoParte.class).addTemplate("valido", new Rule() {{
            add("idParteLegado", random(Long.class, range(1L, 200L)));
            add("tipoParte", random("AAAAAAAA", "ZZZZZZZZ"));
            add("procuradoria", random("AAAAAAAAAAAAAAAAAAAAAAAA", "ZZZZZZZZZZZZZZZZZZZZZZZZ"));
            add("tipoPolo", random(TipoPolo.class));
            add("pessoa", one(Pessoa.class, "valido"));
        }});

        Fixture.of(ProcessoParte.class).addTemplate("validoSemPessoa", new Rule() {{
            add("idParteLegado", random(Long.class, range(1L, 200L)));
            add("tipoParte", random("AAAAAAAA", "ZZZZZZZZ"));
            add("procuradoria", random("AAAAAAAAAAAAAAAAAAAAAAAA", "ZZZZZZZZZZZZZZZZZZZZZZZZ"));
            add("tipoPolo", random(TipoPolo.class));
        }});
    }
}
