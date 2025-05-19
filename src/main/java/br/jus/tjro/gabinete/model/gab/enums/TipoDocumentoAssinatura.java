package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoDocumentoAssinatura {

    Principal("PRINCIPAL"), Anexo("ANEXO"), PedidoInclusaoPauta("PEDIDO_INCLUSAO_PAUTA");

    private final String descricao;

    TipoDocumentoAssinatura(String descricao) {
        this.descricao = descricao;
    }

    public static TipoDocumentoAssinatura converter(String valor) {
        switch (valor) {
            case "Anexo":
                return TipoDocumentoAssinatura.Anexo;
            case "Principal":
                return TipoDocumentoAssinatura.Principal;
            case "PedidoInclusaoPauta":
                return TipoDocumentoAssinatura.PedidoInclusaoPauta;
            default:
                return null;
        }
    }

    public String getDescricao() {
        return descricao;
    }
}
