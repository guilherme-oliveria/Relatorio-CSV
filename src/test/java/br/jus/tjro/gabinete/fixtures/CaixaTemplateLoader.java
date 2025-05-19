package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;

public class CaixaTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Caixa.class).addTemplate("valid", new Rule() {{
            add("id", random(Integer.class, range(1L, 200L)));
            add("nome", "Descricao Nome Caixa");
            add("tamanho", random(Long.class, range(1L, 5000L)));

        }});
    }

}
