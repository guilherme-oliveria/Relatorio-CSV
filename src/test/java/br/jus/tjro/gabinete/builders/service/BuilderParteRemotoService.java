package br.jus.tjro.gabinete.builders.service;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.enums.TipoPolo;
import br.jus.tjro.gabinete.model.gab.transiente.Partes;
import br.jus.tjro.gabinete.service.remoto.ParteRemotoService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BuilderParteRemotoService {

    public ParteRemotoService get() throws Exception {
        setUp();
        ParteRemotoService parteRemotoService = mock(ParteRemotoService.class);
        List<Partes> partes = getPartes();
        when(parteRemotoService.pegarPartesEInteressadosProcessoPorIdProcesso(any(),any())).thenReturn(partes);
        return parteRemotoService;
    }

    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    public List<Partes> getPartes(){
        Partes[] partes= new Partes[4];

        Pessoa pessoa = Fixture.from(Pessoa.class).gimme("valido");

        partes[0] = new Partes("partPoloAtivo",
            "666",
            1l,
            "nomePartPoloAtivo",
            "emailPartPoloAtivo",
            TipoPolo.A,
            "ativo",
            null);
        partes[0].setId(1l);

        partes[1] = new Partes("partPoloAtivoAdvogado",
            "667",
            2l,
            "nomePartPoloAtivoAdvogado",
            "emailPartPoloAtivoAdvogado",
            TipoPolo.A,
            "ativoAdvogado",
            null);
        partes[1].setId(2l);

        partes[2] = new Partes("partPoloPassivo",
            "668",
            3l,
            "nomePartPoloPassivo",
            "emailPartPoloPassivo",
            TipoPolo.P,
            "passivo",
            null);
        partes[2].setId(3l);

        partes[3] = new Partes("partPoloPassivoAdvogado",
            "669",
            4l,
            "nomePartPoloPassivoAdvogado",
            "emailPartPoloPassivoAdvogado",
            TipoPolo.P,
            "passivoAdvogado",
            null);
        partes[3].setId(4l);
        return Arrays.asList(partes);
    }


}
