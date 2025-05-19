package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.service.local.AssinaturaService;

import static org.mockito.Mockito.mock;


public class BuilderAssinaturaService {

    static public AssinaturaService get(){
        return mock(AssinaturaService.class);
    }
}
