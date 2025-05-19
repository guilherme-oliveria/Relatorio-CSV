package br.jus.tjro.gabinete.scheduled.devolve.origem.dto;

import br.jus.tjro.gabinete.api.modelo.DocumentoApi;

public class DocumentoApiDTO extends DocumentoApi {

    private AssinaturaApiDTO assinaturaApiDTO;

    public DocumentoApiDTO() {
    }

    public AssinaturaApiDTO getAssinaturaApiDTO() {
        return assinaturaApiDTO;
    }

    public void setAssinaturaApiDTO(AssinaturaApiDTO assinaturaApiDTO) {
        this.assinaturaApiDTO = assinaturaApiDTO;
    }

}
