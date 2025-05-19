package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.service.local.MinutaAnexoService;

import static org.mockito.Mockito.mock;


public class BuilderMinutaAnexoServiceService {

    public MinutaAnexoService get(){
        return mock(MinutaAnexoService.class);
    }
}
