package br.jus.tjro.gabinete.listener.kafka.dtos;

import java.util.List;

public record PedidoPautaDTO(Long idProcesso, String numeroProcesso, Long idMinuta, String idOrgaoJulgador, String idOrgaoJulgadorColegiado, String tipoVoto, List<PreliminarDTO> preliminares) {
    public PedidoPautaDTO(Long idProcesso, String numeroProcesso, Long idMinuta, String idOrgaoJulgador, String idOrgaoJulgadorColegiado, String tipoVoto, List<PreliminarDTO> preliminares) {
        this.idProcesso = idProcesso;
        this.numeroProcesso = numeroProcesso;
        this.idMinuta = idMinuta;
        this.idOrgaoJulgador = idOrgaoJulgador;
        this.idOrgaoJulgadorColegiado = idOrgaoJulgadorColegiado;
        this.tipoVoto = tipoVoto;
        this.preliminares = preliminares;
    }
}
