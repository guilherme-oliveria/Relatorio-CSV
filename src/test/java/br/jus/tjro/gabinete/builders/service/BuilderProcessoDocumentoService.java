package br.jus.tjro.gabinete.builders.service;

import br.com.six2six.fixturefactory.Fixture;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.ResponsePage;
import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoDocumento;
import br.jus.tjro.gabinete.repository.gab.TipoDocumentoRepository;
import br.jus.tjro.gabinete.repository.gab.processo.ProcessoDocumentoRepository;
import br.jus.tjro.gabinete.scheduled.BuscaMovimentosDosProcessos;
import br.jus.tjro.gabinete.service.local.TipoDocumentoService;
import br.jus.tjro.gabinete.service.local.processo.documento.ProcessoDocumentoService;
import br.jus.tjro.gabinete.service.remoto.ProcessoDocumentoRemotoService;
import br.jus.tjro.gabinete.service.remoto.StorageRemotoService;
import br.jus.tjro.gabinete.service.remoto.kafka.KafkaProducerService;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BuilderProcessoDocumentoService {

    public ProcessoDocumentoService get() throws Exception {
        KafkaProducerService kafkaProducerService = mock(KafkaProducerService.class);
        BuscaMovimentosDosProcessos buscaMovimentosDosProcessos = mock(BuscaMovimentosDosProcessos.class);
        StorageRemotoService storageService = mock(StorageRemotoService.class);
        ProcessoDocumentoService service = new ProcessoDocumentoService(kafkaProducerService, getProcessoDocumentoRemotoService(), getProcessoDocumentoRepository(), storageService);
        return service;
    }

    private TipoDocumentoRepository getTipoDocumentoRespository() {
        TipoDocumentoRepository repository = mock(TipoDocumentoRepository.class);
        when(repository.findById(any())).thenAnswer(
            (Answer<Optional<TipoDocumento>>) invocation -> {
                var tpDoc = new TipoDocumento("1", "Teste", true, false);
                return Optional.of(tpDoc);
            });

        return repository;
    }

    private ProcessoDocumentoRepository getProcessoDocumentoRepository() {
        ProcessoDocumentoRepository repository = mock(ProcessoDocumentoRepository.class);
        when(repository.findByProcesso(any(Processo.class))).thenAnswer(
            (Answer<List<ProcessoDocumento>>) invocation -> {
                Processo processo = invocation.getArgument(0);
                List<ProcessoDocumento> lista = new ArrayList<>();
                if(processo.getFonteDados() == FonteDadosEnum.PJEPG && processo.getId() == 2){
                    ProcessoDocumento doc = Fixture.from(ProcessoDocumento.class).gimme("valido");
                    doc.setIdDocumentoSistemaLegado("11");
                    lista.add(doc);
                }
                return lista;
            });

        when(repository.save(any(ProcessoDocumento.class))).thenAnswer(
            (Answer<ProcessoDocumento>) invocation -> invocation.getArgument(0));

        return repository;
    }

    private ProcessoDocumentoRemotoService getProcessoDocumentoRemotoService() throws Exception {
        ProcessoDocumentoRemotoService service = mock(ProcessoDocumentoRemotoService.class);
        when(service.getDocumentosProcesso(any(Processo.class),any(int.class))).thenAnswer(
            (Answer<ResponsePage<ProcessoDocumento>>) invocation -> {
                Processo processo = invocation.getArgument(0);
                List<ProcessoDocumento> documentos = new ArrayList<ProcessoDocumento>();
                if(processo.getIdProcessoSistemaLegado() == 2l){
                    ProcessoDocumento doc1 = Fixture.from(ProcessoDocumento.class).gimme("valido");
                    doc1.setId(10l);
                    ProcessoDocumento doc2 = Fixture.from(ProcessoDocumento.class).gimme("valido");
                    doc2.setId(11l);
                    documentos.add(doc1);
                    documentos.add(doc2);
                }
                return new ResponsePage(documentos,1,2,0);
            });

        when(service.getDocumento(anyInt(), any(FonteDadosEnum.class))).thenAnswer(
            (Answer<ProcessoDocumento>) invocation -> {
                int id = invocation.getArgument(0);
                FonteDadosEnum fonte = invocation.getArgument(1);
                if(id == 10) {
                    ProcessoDocumento doc1 = Fixture.from(ProcessoDocumento.class).gimme("valido");
                    doc1.setId(10l);
                    return doc1;
                }else if(id == 11){
                    ProcessoDocumento doc2 = Fixture.from(ProcessoDocumento.class).gimme("valido");
                    doc2.setId(11l);
                    return doc2;
                }
                return null;
            });
        return service;
    }

    private TipoDocumentoService getTipoDocumentoService() {
        return mock(TipoDocumentoService.class);
    }
}
