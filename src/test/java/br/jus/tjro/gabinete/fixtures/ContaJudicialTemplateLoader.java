package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.alvara.Conta;
import br.jus.tjro.gabinete.model.gab.alvara.ContaJudicial;
import br.jus.tjro.gabinete.model.gab.alvara.Saldo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ContaJudicialTemplateLoader  implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(ContaJudicial.class).addTemplate("valid", new Rule() {{
            add("codLegado", random(String.class, "1111", "9999999"));
            add("agencia", random(String.class,"1111", "9999"));
            add("numeroConta",  random(String.class,"11111111", "99999999"));
            add("digitoVerificador",  random(String.class,"1", "9"));
            add("codBanco", random(String.class,"1111", "9999"));
            add("produto", name());
            add("valorGuia", random(Double.class,111.00, 9999999.00));
            add("dataPagamentoBoleto", "2022-12-31");
            add("nomeBeneficiario", name());
            add("valorAtualizado", random(Double.class,111.00, 9999999.00));
            add("saldo", one(Saldo.class, "valid"));
        }});
    }
}
