package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.alvara.Conta;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;

public class ContaTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Conta.class).addTemplate("valid", new Rule() {{
            add("banco", firstName());
            add("agencia", random(1111, 9999).toString());
            add("operacao",  "");
            add("numeroConta",  random(String.class,"11111111", "99999999"));
            add("digitoVerificador",  random(String.class, "1", "9"));
        }});
    }
}
