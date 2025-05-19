package br.jus.tjro.gabinete.model.gab.minuta;

public enum MinutaRecebidaStatus {

    PENDENTE("PENDENTE"), FINALIZADA("FINALIZADA"), COM_ERRO("COM_ERRO");

    public String status;

    MinutaRecebidaStatus(String fonte) {
        this.status = fonte;
    }
}
