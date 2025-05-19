package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.service.local.verifica_integracao.VerificaIntegracaoService;

import static org.mockito.Mockito.mock;


public class BuilderVerificaIntegracaoService {

    public VerificaIntegracaoService get(){ return mock(VerificaIntegracaoService.class);
    }
}
