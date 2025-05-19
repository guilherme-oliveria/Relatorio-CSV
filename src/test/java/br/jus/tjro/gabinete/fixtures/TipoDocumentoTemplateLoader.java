package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.config.ColegiadoEnvironment;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;


public class TipoDocumentoTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(TipoDocumento.class).addTemplate("valido", new Rule() {{
            add("id", random(String.class, range(1, 200)));
            add("descricao", random("AAAAAAAA AA AAAAAAAAAAAAAAAA", "ZZZZZZZZ ZZ ZZZZZZZZZZZZZZZZ"));
            add("ativo", random(false, true));
            add("minuta", random(false, true));
        }});

        Fixture.of(TipoDocumento.class).addTemplate("colegiado", new Rule() {{
            add("id", "74");
            add("descricao", random("AAAAAAAA AA AAAAAAAAAAAAAAAA", "ZZZZZZZZ ZZ ZZZZZZZZZZZZZZZZ"));
            add("ativo", random(false, true));
            add("minuta", random(false, true));
        }});
    }
}
