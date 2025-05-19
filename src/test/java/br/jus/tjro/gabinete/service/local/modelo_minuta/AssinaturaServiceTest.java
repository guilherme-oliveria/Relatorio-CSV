package br.jus.tjro.gabinete.service.local.modelo_minuta;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.jus.tjro.assinaturadigital.utils.PKCS7Utils;
import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.config.ColegiadoEnvironment;
import br.jus.tjro.gabinete.interfaces.StorageService;
import br.jus.tjro.gabinete.model.gab.ParametrosEnvioArquivosParaAssinaturaTjOffice;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.enums.TipoDocumentoAssinatura;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.PedidoInclusaoPauta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.PedidoInclusaoPautaRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;
import br.jus.tjro.gabinete.service.assinaturadigital.ValidadorAssinaturaDigitalService;
import br.jus.tjro.gabinete.service.local.*;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
public class AssinaturaServiceTest {

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private RetornoAssinaturaService retornoAssinaturaService;

    @BeforeAll
    public static void setUp() {
        FixtureFactoryLoader.loadTemplates("br.jus.tjro.gabinete.fixtures");
    }

    private AssinaturaServiceMock getMockAssinaturaService(PedidoInclusaoPauta pedido) throws Exception {
        PedidoInclusaoPautaRepository pedidoInclusaoPautaRepository = mock(PedidoInclusaoPautaRepository.class);
        when(pedidoInclusaoPautaRepository.findById(any())).thenReturn(Optional.of(pedido));
        when(pedidoInclusaoPautaRepository.save(pedido)).thenReturn(pedido);

        PKCS7Utils pkcs7Mock = mock(PKCS7Utils.class);
        when(pkcs7Mock.converterParaAtachado(any(byte[].class), any(byte[].class)))
            .thenReturn("Return Converte para atachado".getBytes());
        when(pkcs7Mock.parse(any(byte[].class))).thenReturn(null);

        StorageService storageServiceMock = mock(StorageService.class);
        when(storageServiceMock.store(any(MultipartFile.class))).thenReturn("hashStorage");
        when(storageServiceMock.loadBytes(any(String.class))).thenReturn("file".getBytes());

        ProcessoService processoServiceMock = mock(ProcessoService.class);
        when(processoServiceMock.save(pedido.getMinuta().getProcesso())).thenReturn(pedido.getMinuta().getProcesso());

        return new AssinaturaServiceMock(mock(MinutaService.class), processoServiceMock,
            mock(MinutaAnexoService.class), pkcs7Mock, storageServiceMock, false, pedidoInclusaoPautaRepository);
    }
    private AssinaturaServiceMock getMockAssinaturaService(Minuta minuta) throws Exception {
        ProcessoService processoServiceMock = mock(ProcessoService.class);
        when(processoServiceMock.save(minuta.getProcesso())).thenReturn(minuta.getProcesso());
        MinutaAnexoService minutaAnexoServiceMock = mock(MinutaAnexoService.class);
        when(minutaAnexoServiceMock.getAnexosByminutaPai(minuta)).thenReturn(minuta.getAnexos());
        if (minuta.getAnexos() != null && minuta.getAnexos().size() > 0) {
            MinutaAnexo minutaAnexo = minuta.getAnexos().stream().findFirst().get();
            minutaAnexo.setMinutaPai(minuta);
            if (minutaAnexo != null) {
                when(minutaAnexoServiceMock.findOne(minutaAnexo.getId())).thenReturn(minutaAnexo);
                when(minutaAnexoServiceMock.save(minutaAnexo)).thenReturn(minutaAnexo);
            }
        }
        MinutaService minutaServiceMocked = mock(MinutaService.class);
        when(minutaServiceMocked.findOne(minuta.getId())).thenReturn(minuta);
        when(minutaServiceMocked.salvaHtmlRenderizado(any(Minuta.class), any(String.class))).thenReturn(minuta);
        when(minutaServiceMocked.pegaUltimaMinutaByProcesso(minuta.getProcesso())).thenReturn(minuta);
        when(minutaServiceMocked.salvarComVersao(minuta,new Usuario("windson","cpf",new ArrayList<>(),"token"))).thenReturn(minuta);

        PKCS7Utils pkcs7Mock = mock(PKCS7Utils.class);
        when(pkcs7Mock.converterParaAtachado(any(byte[].class), any(byte[].class)))
            .thenReturn("Return Converte para atachado".getBytes());
        when(pkcs7Mock.parse(any(byte[].class))).thenReturn(null);

        StorageService storageServiceMock = mock(StorageService.class);
        when(storageServiceMock.store(any(MultipartFile.class))).thenReturn("hashStorage");
        when(storageServiceMock.loadBytes(any(String.class))).thenReturn("file".getBytes());

        Set<Minuta> minutas = new HashSet();
        minutas.add(minuta);
        minuta.getProcesso().setMinutas(minutas);

        return new AssinaturaServiceMock(minutaServiceMocked, processoServiceMock,
            minutaAnexoServiceMock, pkcs7Mock, storageServiceMock, false, mock(PedidoInclusaoPautaRepository.class));
    }

    // Methodo com muitos objeto de infra dificil de testar
    @Test
    public void assinaturaDetachedPrincipal() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validParaoAssinatura");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);

        MockMultipartFile assinatura = new MockMultipartFile("teste", "andrew".getBytes());
        assinaturaService.assinaturaDetached(minuta.getId(), assinatura, TipoDocumentoAssinatura.Principal,null);
        assertEquals(minuta.getHashP7s(), "hashStorage");
        assertEquals(minuta.getProcesso().getTarefa(), TarefaEnum.ParaIntegracao);
    }

    @Test
    public void assinaturaDetachedPedidoInclusaoPauta() throws Exception {
        PedidoInclusaoPauta pedido = Fixture.from(PedidoInclusaoPauta.class).gimme("validParaAssinaturaColegiado");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(pedido);
        MockMultipartFile assinatura = new MockMultipartFile("teste", "andrew".getBytes());
        assinaturaService.assinaturaDetached(pedido.getId(), assinatura, TipoDocumentoAssinatura.PedidoInclusaoPauta,null);
        assertEquals("hashStorage", pedido.getHashP7s());
        assertEquals(TarefaEnum.ParaIntegracaoPauta, pedido.getMinuta().getProcesso().getTarefa());
    }

    @Test
    public void naoAssinaSeMinutaNaoForAcordao() throws Exception {
        PedidoInclusaoPauta pedido = Fixture.from(PedidoInclusaoPauta.class).gimme("validParaAssinaturaNaoColegiado");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(pedido);
        MockMultipartFile assinatura = new MockMultipartFile("teste", "andrew".getBytes());

        assertThrows(Exception.class,
            () -> assinaturaService.assinaturaDetached(pedido.getId(), assinatura, TipoDocumentoAssinatura.PedidoInclusaoPauta,null),
            "A minuta deve ser um acórdão");
        assertEquals(pedido.getMinuta().getProcesso().getTarefa(), TarefaEnum.Assinar);
    }

    @Test
    public void assinaturaDetachedPrincipalEhAnexo() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validParaoAssinaturaEhUmAnexo");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);

        MockMultipartFile assinatura = new MockMultipartFile("teste", "andrew".getBytes());
        assinaturaService.assinaturaDetached(minuta.getId(), assinatura, TipoDocumentoAssinatura.Principal,null);
        assertEquals(minuta.getHashP7s(), "hashStorage");
        assertEquals(minuta.getProcesso().getTarefa(), TarefaEnum.Assinando);
        assinaturaService.assinaturaDetached(minuta.getAnexos().stream().findFirst().get().getId(), assinatura,
            TipoDocumentoAssinatura.Anexo,null);
        assertEquals(minuta.getProcesso().getTarefa(), TarefaEnum.ParaIntegracao);
    }

    @Test
    public void testeNaoIniciaAssinaturaSemAnexoEhNaoConcluso() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("invalidNaoConcluso");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);
        assinaturaService.verificaEhPreparaPocessoParaVoltarOrigem(minuta,null);
        //TODO lançar exção
        assertEquals(minuta.getProcesso().getTarefa(), TarefaEnum.NaoConcluso);
    }

    @Test
    public void testeInicioAssinaturaSemAnexo() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validMinutarSemAssinatura");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);
        assinaturaService.verificaEhPreparaPocessoParaVoltarOrigem(minuta,null);
        assertEquals(minuta.getProcesso().getTarefa(), TarefaEnum.Assinando);
    }

    @Test
    public void testeInicioAssinaturaMinutaSemAssEhComUmAnexoSemAss() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validMinutarSemAssinaturaComAnexoSemAssinatura");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);
        assinaturaService.verificaEhPreparaPocessoParaVoltarOrigem(minuta,null);
        assertNotNull(minuta.getAnexos());
        assertEquals(minuta.getProcesso().getTarefa(), TarefaEnum.Assinando);
    }

    @Test
    public void testeInicioAssinaturaComAssEhComUmAnexoSemAss() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validMinutarComAssinaturaComAnexoSemAssinatura");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);
        assinaturaService.verificaEhPreparaPocessoParaVoltarOrigem(minuta,null);
        assertNotNull(minuta.getAnexos());
        assertEquals(minuta.getProcesso().getTarefa(), TarefaEnum.Assinando);
    }

    @Test
    public void testeInicioAssinaturaAnexoComEhSemAssinatura() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validMinutarAnexoComEhSemAssinar");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);
        assinaturaService.verificaEhPreparaPocessoParaVoltarOrigem(minuta,null);
        assertNotNull(minuta.getAnexos());
        assertEquals(minuta.getProcesso().getTarefa(), TarefaEnum.Assinando);
    }

    @Test
    public void testeDevolveOrigemComAssinatura() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validMinutarComAssinatura");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);
        assinaturaService.verificaEhPreparaPocessoParaVoltarOrigem(minuta,null);
        assertEquals(minuta.getProcesso().getTarefa(), TarefaEnum.ParaIntegracao);
    }

    @Test
    public void testeDevolveOrigemComAnexoEhComAssinatura() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validMinutarComAssinaturaComAnexoComAssinatura");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);
        assinaturaService.verificaEhPreparaPocessoParaVoltarOrigem(minuta,null);
        assertNotNull(minuta.getAnexos());
        assertEquals(minuta.getProcesso().getTarefa(), TarefaEnum.ParaIntegracao);
    }

    @Test
    public void testeDevolveOrigemComVariosAnexos() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validMinutarComAssinaturaComAnexosComAssinatura");
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);
        assinaturaService.verificaEhPreparaPocessoParaVoltarOrigem(minuta,null);
        assertNotNull(minuta.getAnexos());
        assertEquals(minuta.getProcesso().getTarefa(), TarefaEnum.ParaIntegracao);
    }

    @Test
    public void validaParmetrosAssinaturaComItensAssinadoEhSemAssinar() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validMinutarAnexoComEhSemAssinar");
        MinutaAnexo minutaAnexo = minuta.getAnexos().stream().filter(p -> p.getHashP7s() == null).findFirst().get();
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);
        Processo processo = minuta.getProcesso();
        List<ParametrosEnvioArquivosParaAssinaturaTjOffice> parametros = assinaturaService.parametrosBuilder(processo);
        assertEquals(5, parametros.size());
    }

    @Test
    public void validaParmetrosAssinaturaComItensAssinadoEhSemAssinarNumaListaDeProcesso() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validMinutarAnexoComEhSemAssinar");
        MinutaAnexo minutaAnexo = minuta.getAnexos().stream().filter(p -> p.getHashP7s() == null).findFirst().get();
        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);
        Processo processo = minuta.getProcesso();
        List<Processo> processos = new ArrayList();
        processos.add(processo);
        List<ParametrosEnvioArquivosParaAssinaturaTjOffice> parametros = assinaturaService.parametrosBuilder(processos);
        assertEquals(5, parametros.size());
    }

    @Test
    public void validaValoresDosParametrosAssinaturaComAnexo() throws Exception {
        Minuta minuta = Fixture.from(Minuta.class).gimme("validParaoAssinaturaEhUmAnexo");
        MinutaAnexo minutaAnexo = minuta.getAnexos().stream().findFirst().get();

        AssinaturaServiceMock assinaturaService = getMockAssinaturaService(minuta);
        Processo processo = minuta.getProcesso();
        List<ParametrosEnvioArquivosParaAssinaturaTjOffice> parametros = assinaturaService.parametrosBuilder(processo);
        assertEquals(2, parametros.size());
        for (ParametrosEnvioArquivosParaAssinaturaTjOffice p : parametros) {
            if (p.getParamsEnvio().toString().indexOf("Anexo") > 0) {
                assertThat(p.getNome().replaceAll("_", "")).contains(minutaAnexo.getDescricao().replaceAll("\\s", ""));
                assertThat(p.getParamsEnvio().toString()).contains("tipoDocumento=Anexo");
                assertThat(p.getParamsEnvio().toString()).contains("id=" + minutaAnexo.getId());
            } else {
                assertThat(p.getParamsEnvio().toString()).contains("tipoDocumento=Principal");
                assertThat(p.getParamsEnvio().toString()).contains("id=" + minuta.getId());
            }
        }

    }

    public class AssinaturaServiceMock extends AssinaturaService {

        private final RetornoAssinaturaService retornoAssinaturaService;
        public AssinaturaServiceMock(MinutaService minutaService,
                                    ProcessoService processoService, MinutaAnexoService minutaAnexoService, PKCS7Utils pkcs7,
                                    StorageService storageService, boolean validaAssinatura, PedidoInclusaoPautaRepository pedidoInclusaoPautaRepository) {
            super(minutaService,
                processoService,
                minutaAnexoService,
                pkcs7,
                storageService,
                validaAssinatura,
                mock(KafkaProducerService.class),
                mock(MinutaTarefaLogService.class),
                pedidoInclusaoPautaRepository);
            retornoAssinaturaService = new RetornoAssinaturaService(processoService,minutaService,minutaAnexoService,pedidoInclusaoPautaRepository,
                mock(ProcessosRepository.class), false,
                storageService,mock(ValidadorAssinaturaDigitalService.class),mock(MinutaTarefaLogService.class),pkcs7);
        }

        @Override
        public String getUrlGabinete() {
            return "url";
        }

        public Processo verificaEhPreparaPocessoParaVoltarOrigem(Minuta minuta, Usuario usuario) throws Exception {
            return retornoAssinaturaService.verificaEhPreparaPocessoParaVoltarOrigem(minuta,usuario);
        }

        public void assinaturaDetached(Long id, MockMultipartFile assinatura, TipoDocumentoAssinatura principal, Usuario usuario) throws Exception {
            retornoAssinaturaService.assinaturaDetached(id,assinatura,principal,usuario);
        }
    }
}
