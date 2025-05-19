package br.jus.tjro.gabinete.service.remoto.sg;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class PautaColegiadoRetorno {
    private String idProcesso;

    private HashMap<String, String> documentos;

    @JsonCreator
    public PautaColegiadoRetorno(@JsonProperty("idProcesso") String idProcesso, @JsonProperty("documentos") HashMap<String, String> documentos) {
        this.idProcesso = idProcesso;
        this.documentos = documentos;
    }

    public String getLegado(Long id) {
        return this.documentos.get(id.toString());
    }
}
