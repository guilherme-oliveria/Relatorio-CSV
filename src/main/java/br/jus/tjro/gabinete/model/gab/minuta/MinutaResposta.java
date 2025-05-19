package br.jus.tjro.gabinete.model.gab.minuta;

import br.jus.tjro.gabinete.model.gab.enums.SistemaOrigemEnum;

import java.io.Serializable;

public class MinutaResposta implements Serializable {

    private final SistemaOrigemEnum sistemaOrigem;
    private final String referenciaOrigem;
    private final MinutaRecebidaStatus status;

    public MinutaResposta(SistemaOrigemEnum sistemaOrigem, String referenciaOrigem, MinutaRecebidaStatus status) {
        this.sistemaOrigem = sistemaOrigem;
        this.referenciaOrigem = referenciaOrigem;
        this.status = status;
    }

    public SistemaOrigemEnum getSistemaOrigem() {
        return sistemaOrigem;
    }

    public String getReferenciaOrigem() {
        return referenciaOrigem;
    }

    public MinutaRecebidaStatus getStatus() {
        return status;
    }
}
