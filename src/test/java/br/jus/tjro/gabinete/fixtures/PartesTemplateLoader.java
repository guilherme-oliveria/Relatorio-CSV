package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.transiente.EnderecoTransiente;
import br.jus.tjro.gabinete.model.gab.transiente.ParteEnderecoTransiente;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;

public class PartesTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Partes.class).addTemplate("valido", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
        }});
    }
}
