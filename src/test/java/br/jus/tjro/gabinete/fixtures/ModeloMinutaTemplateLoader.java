package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinuta;

public class ModeloMinutaTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(ModeloMinuta.class).addTemplate("valido", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("descricao", random("Minuta antes de renderizar"));
            add("template", random("Minuta renderizado"));
            add("idOrgaoJulgador", 1l);
        }});
    }
}
