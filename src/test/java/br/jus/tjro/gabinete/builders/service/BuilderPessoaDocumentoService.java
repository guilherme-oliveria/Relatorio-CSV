package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.endereco.Estado;
import br.jus.tjro.gabinete.model.gab.pessoa.PessoaDocumento;
import br.jus.tjro.gabinete.model.gab.transiente.PessoaDocumentosTransiente;
import br.jus.tjro.gabinete.repository.gab.pessoa.PessoaDocumentoRepository;
import br.jus.tjro.gabinete.service.local.EstadoService;
import br.jus.tjro.gabinete.service.local.PessoaDocumentoService;
import br.jus.tjro.gabinete.service.local.PessoaService;
import br.jus.tjro.gabinete.service.remoto.PessoaDocumentosRemotoService;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Random;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BuilderPessoaDocumentoService {

    public PessoaDocumentoService get() throws Exception {
        PessoaDocumentoService service = new PessoaDocumentoService(
            getPessoaDocumentosRemotoService(),
            getPessoaDocumentoRepository(),
            getPessoaService(),
            getEstadoService());
        return service;
    }

    private EstadoService getEstadoService() {
        BuilderEstadoService builder = new BuilderEstadoService();
        return builder.get();
    }

    private PessoaService getPessoaService() {
        BuilderPessoaService builder = new BuilderPessoaService();
        return builder.get();
    }

    private PessoaDocumentoRepository getPessoaDocumentoRepository(){
        PessoaDocumentoRepository repository = mock(PessoaDocumentoRepository.class);
        when(repository.saveAll(anyList())).thenAnswer(
            (Answer<List<PessoaDocumento>>) invocation -> {
                List<PessoaDocumento> lista;
                lista = invocation.getArgument(0);
                lista.stream().forEach(p -> p.setId(new Random().nextLong()));
                return lista;
            });
        return repository;
    }

    private PessoaDocumentosRemotoService getPessoaDocumentosRemotoService() throws Exception {
        PessoaDocumentosRemotoService repository = mock(PessoaDocumentosRemotoService.class);
        when(repository.buscaDocumentosDaPessoa(any(Long.class),any(FonteDadosEnum.class))).thenAnswer(
            (Answer<PessoaDocumentosTransiente[]>) invocation -> {
                Long id = invocation.getArgument(0);
                FonteDadosEnum fonte = invocation.getArgument(1);
                return listaDePessoaDocumentoRemoto(id,fonte);
            });
        return repository;
    }

    private PessoaDocumentosTransiente[] listaDePessoaDocumentoRemoto(Long id, FonteDadosEnum fonteDadosEnum){
        PessoaDocumentosTransiente[] lista;
        if(fonteDadosEnum == FonteDadosEnum.PJEPG && id == 6) {
            lista = new PessoaDocumentosTransiente[1];
            lista[0] = new PessoaDocumentosTransiente();
            lista[0].setEhPrincipal(true);
            lista[0].setEstado(new Estado());
            lista[0].setIdPessoaDocIdentificacao(66l);
            lista[0].setIdPessoaLegado(6l);
            lista[0].setNrDocumento("numeroDocumento");
            lista[0].setTipoDocumento("CPF");
        }
        else if(fonteDadosEnum == FonteDadosEnum.PJESG && id == 6)
            lista = new PessoaDocumentosTransiente[0];
        else
            lista = new PessoaDocumentosTransiente[0];
        return lista;
    }


}
