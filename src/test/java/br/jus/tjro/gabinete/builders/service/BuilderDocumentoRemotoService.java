package br.jus.tjro.gabinete.builders.service;

import br.jus.tjro.gabinete.service.remoto.DocumentoRemotoService;

import static org.mockito.Mockito.mock;


public class BuilderDocumentoRemotoService {

    public DocumentoRemotoService get(){
        return mock(DocumentoRemotoService.class);
    }
}
