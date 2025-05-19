package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.model.gab.alvara.Conta;
import br.jus.tjro.gabinete.model.gab.alvara.ContaJudicial;
import br.jus.tjro.gabinete.model.gab.alvara.PagamentoAlvara;
import br.jus.tjro.gabinete.model.gab.alvara.Saldo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class PagamentoAlvaraTemplateLoader implements TemplateLoader {
    @Override
    public void load() {
        Fixture.of(PagamentoAlvara.class).addTemplate("valid", new Rule() {{
            add("nomeFavorecido", name());
            add("docFavorecido",  random(String.class,"1111", "9999"));
            add("conta", one(Conta.class, "valid"));
            add("formaPagamento", "T");
            add("comAtualizacao", true);
            add("valor", random(BigDecimal.class,new BigDecimal("111.00"), new BigDecimal("9999999.00")));
            add("contaJudicial", one(ContaJudicial.class, "valid"));
            add("enviado", false);
            add("idFavorecido", random(String.class,"1111", "9999"));
            add("nomeSacador", name());
            add("documentoSacador",  random(String.class, "1111", "9999"));
            add("nomeSacador2", name());
            add("documentoSacador2",  random(String.class, "1111", "9999"));
            add("validade", random(Integer.class, 1, 99));
        }});
    }
}
