package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.alvara.ContaJudicial;
import br.jus.tjro.gabinete.model.gab.alvara.Saldo;

import java.text.SimpleDateFormat;

public class SaldoTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(Saldo.class).addTemplate("valid", new Rule() {{
            add("valor", random(Double.class,111.00, 9999999.00));
            add("dataAtualizacao", randomDate("2020-01-01", "2022-12-31", new SimpleDateFormat("yyyy-MM-dd")));
        }});
    }
}
