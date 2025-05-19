package br.jus.tjro.gabinete.fixtures;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.jus.tjro.gabinete.fonte.dados.PjePG;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.enums.TipoPessoaEnum;

import java.text.SimpleDateFormat;


public class PessoaTemplateLoader implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Pessoa.class).addTemplate("valido", new Rule() {{
            add("idPessoaLegado", random(Long.class, range(1L, 200L)));
            add("nome", random("AAAAAAAA AA AAAAAAAAAAAAAAAA", "ZZZZZZZZ ZZ ZZZZZZZZZZZZZZZZ"));
            add("email", random("AAAAAAAA@AAAAAAAA.com", "ZZZZZZZZ@ZZZZZZZZ.com"));
            add("dataNascimento", randomDate("1900-01-01", "2017-12-31",
                new SimpleDateFormat("yyyy-MM-dd")));
            add("dataObito", randomDate("1900-01-01", "2017-12-31",
                new SimpleDateFormat("yyyy-MM-dd")));
            add("tipoPessoa", random(TipoPessoaEnum.class));
            add("sistema", FonteDadosEnum.PJEPG);
        }});
    }
}
