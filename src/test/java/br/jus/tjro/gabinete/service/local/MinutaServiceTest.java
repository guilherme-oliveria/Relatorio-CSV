package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.builders.CaixaBuilder;
import br.jus.tjro.gabinete.builders.DocumentoBuilder;
import br.jus.tjro.gabinete.builders.ProcessoBuilder;
import br.jus.tjro.gabinete.interfaces.ModeloDocumentoService;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.minuta.PublicarProcessoDJE;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.MinutaEmLote;
import br.jus.tjro.gabinete.repository.gab.localizador.CaixasRepository;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutaMovimentoRepository;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessosRepository;
import br.jus.tjro.gabinete.service.local.tpu.TpuMovimentoService;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = "conta.centralizadora={\"banco\": \"104\",\"agencia\": \"2848\",\"operacao\": \"040.1\",\"numeroConta\": \"01529904\",\"digitoVerificador\": \"5\",\"numeroProcesso\":\"00759114820108221111\",\"idLegado\":76605}")
@AutoConfigureEmbeddedDatabase
public class MinutaServiceTest {

    static Long id = 0l;
    @Autowired
    private ProcessoService processoService;
    @Autowired
    private CaixasRepository caixasRepository;
    @Autowired
    private MinutaService documentoService;
    @Autowired
    private ProcessosRepository processosRepository;
    @Autowired
    private MinutasRepository documentosRepository;
    @Autowired
    private MinutaService minutaService;

    private Processo getProcesso() throws Exception {
        Caixa caixa = getCaixa();
        id++;

        Processo p = this.processoService.save(ProcessoBuilder.umProcesso().idLegado(id).caixa(caixa)
            .tarefa(TarefaEnum.Minutar).orgaoJulgador("PJEPG-1").agora());
        return p;
    }

    private Caixa getCaixa() {
        Caixa caixa = this.caixasRepository.save(CaixaBuilder.umaCaixa().agora());
        return caixa;
    }

    @Test
    public void documentoSemProcessoSemId() {
        assertThrows(RuntimeException.class, () -> {
            Processo p = new Processo();
            Minuta documento = DocumentoBuilder.umDocumento(p).build();
            documentoService.save(documento);
        });
    }

    @Test
    public void salvaDoisDocumentosParaProcessoPoremUmEstaAssinado() throws Exception {
        Processo p = getProcesso();

        Minuta documento = DocumentoBuilder.umDocumento(p).build();
        documento.setHashP7s("hashMock");
        try {
            documento = documentoService.save(documento);
            assertEquals(documentoService.findOne(documento.getId()), documento);

            Minuta documento1 = DocumentoBuilder.umDocumento(p).build();
            documento1 = documentoService.salvarComVersao(documento1,new Usuario("windson","cpf",new ArrayList<>(),"token"));
            assertEquals(documentoService.findOne(documento1.getId()), documento1);
        } catch (Exception e) {

        }
    }

    @Test
    public void testaAdicionarCabecalhoEmMinutaSemCabecalho() throws Exception {
        Processo processo = getProcesso();
        Minuta documento = DocumentoBuilder.umDocumento(processo).build();
        documento.setMinutaHtml(null);
        documentoService.setCabecalho(documento);
        assertNotNull(documento.getMinutaHtml());
    }

    @Test()
    public void testaAdicionaMovimentoEmMinutaEmLote() throws Exception {
        MinutaMovimento minutaMovimento = minutaService.adicionaMovimento(0l, 12035l);
        assertNotNull(minutaMovimento);
    }

    @Test()
    public void testaAdicionaMovimentoEmMinuta() throws Exception {
        MinutaService mService = getUmaMinutaService();
        MinutaMovimento minutaMovimento = mService.adicionaMovimento(666l, 12035l);
        assertNotNull(minutaMovimento);
    }

    @Test()
    public void testaMinutaEmLote() throws Exception {
        MinutaService minutaService = getUmaMinutaService();
        Minuta minutaDoLote = new Minuta();
        minutaDoLote.setMinutaHtml("minuta em lote");
        minutaDoLote.setPublicacaoDje(new PublicarProcessoDJE());
        TipoDocumento tipoDocumento = new TipoDocumento("id","descricao",true,true);
        minutaDoLote.setTipoDocumento(tipoDocumento);
        Map<Long, List<MinutaMovimento>> mapMinutaMovimentos = Map.of(666l, List.of(new MinutaMovimento(minutaDoLote, 12035l)));
        minutaDoLote.setMinutaMovimentos(mapMinutaMovimentos.get(666l));
        MinutaEmLote minutaEmLote = new MinutaEmLote(minutaDoLote, mapMinutaMovimentos);
        List<Long> resp = minutaService.minutarEmLote(getUsuario(), minutaEmLote);
        assertTrue(resp.contains(666L));
    }

    private Usuario getUsuario() {
        return new Usuario("Luci", "666666", List.of(""), "token");
    }

    private MinutaService getUmaMinutaService() throws Exception {
        MinutasRepository minutasRepository = mock(MinutasRepository.class);
        MinutaMovimentoRepository minutaMovimentoRepository = mock(MinutaMovimentoRepository.class);
        MinutaVersaoService minutaVersaoService = mock(MinutaVersaoService.class);
        TpuMovimentoService tpuMovimentoService = mock(TpuMovimentoService.class);
        PublicarProcessoDjeService publicarProcessoDjeService = mock(PublicarProcessoDjeService.class);
        ModeloDocumentoService modeloDocumento = mock(ModeloDocumentoService.class);
        ProcessoService processoService = mock(ProcessoService.class);
        ProcessosRepository processosRepository = mock(ProcessosRepository.class);
        ModeloMinutaService modeloMinutaService = mock(ModeloMinutaService.class);
        MinutaAnexoService minutaAnexoService = mock(MinutaAnexoService.class);

        when(minutaMovimentoRepository.saveAll(any())).thenReturn(List.of(new MinutaMovimento(new Minuta(666l), 12035l)));
        when(publicarProcessoDjeService.save(any())).thenReturn(null);
        when(modeloDocumento.renderizarVariavel(any(),any(),any())).thenReturn("MINUTADO");
        when(modeloDocumento.renderizarVariavel(any(),any(),any(),any())).thenReturn("MINUTADO");
        when(minutaMovimentoRepository.save(any())).thenReturn(new MinutaMovimento());
        when(minutaMovimentoRepository.findByMinuta(any())).thenReturn(List.of(new MinutaMovimento()));

        Processo processo = new Processo(666l);
        Minuta minuta = new Minuta(666l);
        minuta.setMinutaHtml("TESTE");
        processo.setMinutas(Set.of(minuta));
        minuta.setProcesso(processo);
        when(processosRepository.findById(any())).thenReturn(Optional.of(processo));
        when(publicarProcessoDjeService.verificaEhRemovePublicacaoDje(any())).thenReturn(minuta);
        when(minutaVersaoService.minutaVersaoComparaEhSalva(minuta, getUsuario())).thenReturn(1);
        when(minutasRepository.save(any())).thenReturn(minuta);
        return new MinutaService(minutasRepository, minutaMovimentoRepository, minutaVersaoService,
            tpuMovimentoService, publicarProcessoDjeService, processoService,
            processosRepository, modeloMinutaService,modeloDocumento, minutaAnexoService);
    }


    @Test
    public void testaSetMovimentosEhComplementosFromTpu() throws Exception {
        Minuta minuta = new Minuta();
        MinutaMovimento minutaMovimento = new MinutaMovimento(minuta, 12035l);
        List<MinutaMovimento> movimentos = List.of(minutaMovimento);
        minuta.setMinutaMovimentos(movimentos);
        minutaService.setMovimentosEhComplementosFromTpu(minuta);
        assertNotNull(minuta.getMinutaMovimentos().get(0).getDescricao());
    }

    @AfterEach
    public void tearDownAfter() throws Exception {
        this.documentosRepository.deleteAll();
        this.processosRepository.deleteAll();
        this.caixasRepository.deleteAll();
    }
}
