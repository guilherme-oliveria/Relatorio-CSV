package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoNotificacaoEnum {
    MENSAGEM("mensagem"), PROCESSO_ATUALIZADO("processo_atualizado");
    public String tipo;
    TipoNotificacaoEnum (String tipo) {
        this.tipo = tipo;
    }
}
