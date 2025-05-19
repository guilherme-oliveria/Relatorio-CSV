package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;

import static org.mockito.Mockito.mock;

public class BuilderRetornoAssinaturaService {

    public static RetornoAssinaturaService get(){
        return mock(RetornoAssinaturaService.class);
    }
}
