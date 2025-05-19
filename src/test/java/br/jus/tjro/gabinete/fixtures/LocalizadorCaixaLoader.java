package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.enums.TipoDeLocalizadorEnum;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorCaixa;

public class LocalizadorCaixaLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(LocalizadorCaixa.class).addTemplate("valid", new Rule() {{
            add("id", random(Long.class, range(1L, 200L)));
            add("nome", "Descricao Nome Localizador Caixa");
            add("tamanho", random(Long.class, range(1L, 5000L)));
            add("tipo", TipoDeLocalizadorEnum.Usuario);

        }});
        Fixture.of(LocalizadorCaixa.class).addTemplate("validIdMaiorQueUm", new Rule() {{
            add("id", random(Long.class, range(3L, 200L)));
            add("nome", "Descricao Nome Localizador Caixa");
            add("tamanho", random(Long.class, range(1L, 5000L)));
            add("tipo", TipoDeLocalizadorEnum.Usuario);

        }});
    }

}
