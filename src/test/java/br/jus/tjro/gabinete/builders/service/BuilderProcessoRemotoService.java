package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.api.RequisicaoRetornoConcluso;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.service.remoto.processo.ProcessoRemotoService;
import org.mockito.stubbing.Answer;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BuilderProcessoRemotoService {

    public ProcessoRemotoService get() throws Exception {
        ProcessoRemotoService mock = mock(ProcessoRemotoService.class);
        when(mock.devolveProcessoOrigem(any(RequisicaoRetornoConcluso.class),any())).thenAnswer(
            (Answer<String>) invocation -> {
                RequisicaoRetornoConcluso requisicao = invocation.getArgument(0);
                if("erro".equals(requisicao.getDocumentoApi().getContentType()))
                    throw new Exception("mock erro");
                new Random().nextLong();
                return "";
            });
        return mock;
    }
}
