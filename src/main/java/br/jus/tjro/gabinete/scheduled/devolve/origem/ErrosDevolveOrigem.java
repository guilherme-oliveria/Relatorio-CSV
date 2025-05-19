package br.jus.tjro.gabinete.scheduled.devolve.origem;

public enum  ErrosDevolveOrigem {

    SemMinutaParaIntegracaoEhProcessoNaoConcluso("SemMinutaParaIntegracaoEhProcessoNaoConcluso"),
    FalhaRecuperarAssinaturaStorage("FalhaRecuperarAssinaturaStorage"),
    SemMovimento("SemMovimento"),
    B("Sistema");

    private final String codigoErro;

    ErrosDevolveOrigem(String codigoErro) {
        this.codigoErro = codigoErro;
    }
}
