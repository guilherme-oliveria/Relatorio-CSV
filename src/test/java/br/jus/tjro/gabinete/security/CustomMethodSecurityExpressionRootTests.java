package br.jus.tjro.gabinete.security;

import br.jus.tjro.gabinete.config.AdminsEnvironment;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.remoto.ModeloDocumentoRepository;
import br.jus.tjro.gabinete.service.remoto.VisibilidadeRemotoService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomMethodSecurityExpressionRootTests {
    AdminsEnvironment admins = new AdminsEnvironment(Collections.singletonList(""));
    Usuario usuario = new Usuario("windson", "cpfWindson", List.of("administrador"), "token");

    public CustomMethodSecurityExpressionRoot getSecurity() {
        return getSecurity(null);
    }

    public CustomMethodSecurityExpressionRoot getSecurity(Usuario user) {
        Authentication auth = user == null ? mock(Authentication.class) :
            new UsernamePasswordAuthenticationToken(user.getId(), user, user.authority());
        VisibilidadeRemotoService visbilidadeMock = mock(VisibilidadeRemotoService.class);
        when(visbilidadeMock.temVisibilidade(any(), any())).thenReturn(false);
        AuthenticationUsuarioService autheServiceMock = mock(AuthenticationUsuarioService.class);
        when(autheServiceMock.getUsuario(any())).thenReturn(usuario);
        return new CustomMethodSecurityExpressionRoot(auth, visbilidadeMock,
            autheServiceMock, mock(ModeloDocumentoRepository.class), admins);
    }

    public CustomMethodSecurityExpressionRoot getSecuritySegredoDisable() {
        VisibilidadeRemotoService visbilidadeMock = mock(VisibilidadeRemotoService.class);
        AuthenticationUsuarioService autheServiceMock = mock(AuthenticationUsuarioService.class);
        when(autheServiceMock.getUsuario(any())).thenReturn(usuario);
        return new CustomMethodSecurityExpressionRoot(mock(Authentication.class), visbilidadeMock, true,
            autheServiceMock, mock(ModeloDocumentoRepository.class), admins, new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>());
    }

    public CustomMethodSecurityExpressionRoot getSecurityVisibilidadeTrue() {
        VisibilidadeRemotoService visbilidadeMock = mock(VisibilidadeRemotoService.class);
        when(visbilidadeMock.temVisibilidade(any(), any())).thenReturn(true);
        AuthenticationUsuarioService autheServiceMock = mock(AuthenticationUsuarioService.class);
        when(autheServiceMock.getUsuario(any())).thenReturn(usuario);
        return new CustomMethodSecurityExpressionRoot(mock(Authentication.class), visbilidadeMock, autheServiceMock, mock(ModeloDocumentoRepository.class), admins);
    }

    @Test
    public void validaAoEnviarProcessoComOptionalEmpty() {
        assertTrue(getSecurity().validaProcessoAcessado(Optional.empty()));
    }


    @Test
    public void verificaQuandoNaoTemSegredo() {
        Processo processo = new Processo();
        processo.setSegredoJustica(false);
        assertTrue(getSecurity().verificaSeAcessaProcessoSigiloso(processo));
    }

    @Test
    public void verificaQuandoTemSegredo() {
        OrgaoJulgador orgaoJulgador = new OrgaoJulgador("PJEPG-88",
            "Orgão Julgador", List.of(), "sigla2", List.of());
        orgaoJulgador.setSistema("PJEPG");

        Processo processo = new Processo();
        processo.setOrgaoJulgadorObj(orgaoJulgador);
        processo.setSegredoJustica(true);
        assertFalse(getSecurity().verificaSeAcessaProcessoSigiloso(processo));
    }

    @Test
    public void verificaQuandoTemSegredoDisablle() {
        Processo processo = new Processo();
        processo.setTpuClasse(new TpuClasse(666L));
        processo.setSegredoJustica(true);
        assertTrue(getSecuritySegredoDisable().verificaSeAcessaProcessoSigiloso(processo));
    }

    @Test
    public void verificaQuandoTemSegredoEhVisibilidade() {
        OrgaoJulgador orgaoJulgador = new OrgaoJulgador("PJEPG-88",
            "Orgão Julgador", List.of(), "sigla2", List.of());
        orgaoJulgador.setSistema("PJEPG");

        Processo processo = new Processo();
        processo.setOrgaoJulgadorObj(orgaoJulgador);
        processo.setSegredoJustica(true);
        assertTrue(getSecurityVisibilidadeTrue().verificaSeAcessaProcessoSigiloso(processo));
    }

    @Test
    public void verificaSeAssessorPodeAcessarProcessoSigilosoNivel1(){
        OrgaoJulgador orgaoJulgador = new OrgaoJulgador("PJEPG-88",
            "Orgão Julgador", List.of(), "sigla2", List.of());
        orgaoJulgador.setSistema("PJEPG");
        Papel papel = new Papel("assessor", List.of(orgaoJulgador));

        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel));
        usuario.setPapeis(mapPapeis);

        Processo processo = new Processo();
        processo.setSegredoJustica(true);
        processo.setOrgaoJulgadorObj(orgaoJulgador);
        processo.setSistema(FonteDadosEnum.PJEPG);
        processo.setNivelSigilo(1);

        assertTrue(getSecurity(usuario).verificaSeAcessaProcessoSigiloso(processo));
    }

    @Test
    public void verificaSeAssessorPodeAcessarProcessoSigilosoNivel5(){
        OrgaoJulgador orgaoJulgador = new OrgaoJulgador("PJEPG-88",
            "Orgão Julgador", List.of(), "sigla2", List.of());
        orgaoJulgador.setSistema("PJEPG");
        Papel papel = new Papel("assessor", List.of(orgaoJulgador));

        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel));
        usuario.setPapeis(mapPapeis);

        Processo processo = new Processo();
        processo.setSegredoJustica(true);
        processo.setOrgaoJulgadorObj(orgaoJulgador);
        processo.setSistema(FonteDadosEnum.PJEPG);
        processo.setNivelSigilo(5);

        assertFalse(getSecurity(usuario).verificaSeAcessaProcessoSigiloso(processo));
    }

    @Test
    public void verificaSeUsuarioSemRecursoPodeAcessarProcessoSigiloso(){
        OrgaoJulgador orgaoJulgador = new OrgaoJulgador("PJEPG-88",
            "Orgão Julgador", List.of(), "sigla2", List.of());
        orgaoJulgador.setSistema("PJEPG");
        Papel papel = new Papel("diretor", List.of(orgaoJulgador));

        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel));
        usuario.setPapeis(mapPapeis);

        Processo processo = new Processo();
        processo.setSegredoJustica(true);
        processo.setOrgaoJulgadorObj(orgaoJulgador);
        processo.setSistema(FonteDadosEnum.PJEPG);

        assertFalse(getSecurity(usuario).verificaSeAcessaProcessoSigiloso(processo));
    }
    @Test
    public void verificaProcessoSegredoOutroOrgaoJulgadorNivel5() {
        OrgaoJulgador orgaoJulgadorProcesso = new OrgaoJulgador("PJEPG-77",
            "Magistrado nao pode ter esse Orgao", List.of(), "sigla", List.of());
        orgaoJulgadorProcesso.setSistema("PJEPG");

        OrgaoJulgador orgaoJulgadorUsuario = new OrgaoJulgador("PJEPG-88",
            "OJ do usuario", List.of(), "sigla2", List.of());
        orgaoJulgadorUsuario.setSistema("PJEPG");

        Processo processo = new Processo();
        processo.setSegredoJustica(true);
        processo.setOrgaoJulgadorObj(orgaoJulgadorProcesso);
        processo.setSistema(FonteDadosEnum.PJEPG);
        processo.setNivelSigilo(5);

        Papel papel = new Papel("magistrado", List.of(orgaoJulgadorUsuario));
        Map<String, List<Papel>> mapPapeis = new HashMap<String, List<Papel>>();
        mapPapeis.put("PJEPG", List.of(papel));

        usuario.setPapeis(mapPapeis);
        assertFalse(getSecurity(usuario).verificaSeAcessaProcessoSigiloso(processo));
    }


    @Test
    public void naoPodeChamarVerificacaoVisbilidadeQuandoNaoTemSegredo() {
        OrgaoJulgador orgaoJulgador = new OrgaoJulgador("PJEPG-88",
            "Orgão Julgador", List.of(), "sigla2", List.of());
        orgaoJulgador.setSistema("PJEPG");

        VisibilidadeRemotoService visbilidadeMock = mock(VisibilidadeRemotoService.class);
        when(visbilidadeMock.temVisibilidade(any(), any())).thenReturn(true);
        AuthenticationUsuarioService autheServiceMock = mock(AuthenticationUsuarioService.class);
        when(autheServiceMock.getUsuario(any())).thenReturn(usuario);
        CustomMethodSecurityExpressionRoot serviceMock = new CustomMethodSecurityExpressionRoot(mock(Authentication.class), visbilidadeMock, autheServiceMock, mock(ModeloDocumentoRepository.class), admins);

        Processo processo = new Processo();
        processo.setOrgaoJulgadorObj(orgaoJulgador);
        processo.setSegredoJustica(true);
        assertTrue(processo.isSegredoJustica());
        verify(visbilidadeMock, atMost(0)).temVisibilidade(any(), any());
        assertTrue(serviceMock.verificaSeAcessaProcessoSigiloso(processo));
    }
}
