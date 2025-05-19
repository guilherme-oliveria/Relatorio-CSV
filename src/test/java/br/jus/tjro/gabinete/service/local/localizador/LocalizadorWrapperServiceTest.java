package br.jus.tjro.gabinete.service.local.localizador;

import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorCaixa;
import br.jus.tjro.gabinete.model.gab.localizador.LocalizadorWrapper;
import br.jus.tjro.gabinete.model.gab.localizador.RegraWrapper;
import br.jus.tjro.gabinete.model.gab.segredo.recurso.AdministradorRecurso;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.localizador.caixa.LocalizadorCaixaRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class LocalizadorWrapperServiceTest {
    public LocalizadorWrapperService getLocalizadorWrapperService() throws Exception {

        LocalizadorCaixaRepository localizadorCaixaRepository = mock(LocalizadorCaixaRepository.class);
        when(localizadorCaixaRepository.findById(anyLong())/* ToDo testar o null*/).thenReturn(null);

        ValidaLocalizador validaLocalizador = mock(ValidaLocalizador.class);

        doNothing().when(validaLocalizador).validaLocalizadorAntesDeSalvar(any(String.class), any(LocalizadorWrapper.class), any(Usuario.class));

        return new LocalizadorWrapperService(localizadorCaixaRepository, validaLocalizador);
    }

    public List<RegraWrapper> getRegrasWrapperProcessoPartesNome() {
        List<RegraWrapper> regras = new ArrayList();

        RegraWrapper regra = new RegraWrapper();
        regra.setOperador("=");
        regra.setTipo("processoPartes.nome");
        regra.setValor("ANDREW RAMIRES MAY");
        regras.add(regra);
        return regras;
    }

    private LocalizadorWrapper getLocalizadorWrapper() {
        LocalizadorWrapper localizadorWrapper = new LocalizadorWrapper();
        localizadorWrapper.setDeOrgaoJulgador(false);
        localizadorWrapper.setExclusivo(false);
        localizadorWrapper.setOrgaoJulgador("666");
        localizadorWrapper.setRegras(getRegrasWrapperProcessoPartesNome());
        localizadorWrapper.setNome("realFakeLocalizador");
        return localizadorWrapper;
    }

    @Test
    public void testaRegraWrapper() throws Exception {
        LocalizadorWrapperService service = getLocalizadorWrapperService();
        LocalizadorWrapper localizadorWrapper = getLocalizadorWrapper();

        OrgaoJulgador orgao = new OrgaoJulgador("PJEPG-666");

        List<OrgaoJulgador> orgaoJulgadores = new ArrayList<>();
        orgaoJulgadores.add(orgao);

        Papel papel = new Papel("Administrador", orgaoJulgadores);

        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel));
        Usuario usuario = new Usuario("Andrew", "16876008053", null, "tokenWrapper");
        usuario.setPapeis(mapPapeis);

        LocalizadorCaixa localizadorCaixa = service.wrapperForLocalizadorCaixa(localizadorWrapper, usuario);

        assertEquals(localizadorCaixa.getNome(), localizadorWrapper.getNome());
    }
}
