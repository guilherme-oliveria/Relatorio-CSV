package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoAssunto;


public class ProcessoAssuntoTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(ProcessoAssunto.class).addTemplate("valido", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("idAssunto", random(Long.class, range(1L, 200L)));
            add("isAssuntoPrincipal", random(false, true));
        }});

        Fixture.of(ProcessoAssunto.class).addTemplate("validoSemId", new Rule() {{
            add("idAssunto", random(Long.class, range(1L, 200L)));
            add("isAssuntoPrincipal", random(false, true));
        }});
    }
}
