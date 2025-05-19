package br.jus.tjro.gabinete.model.gab.alvara;

public class GerarDepositoEAlvara27Request {
    private GerarDepositoRequest gerarDepositoRequest;
    private GerarAlvara27Request gerarAlvara27Request;

    public GerarDepositoEAlvara27Request() {
    }

    public GerarDepositoEAlvara27Request(GerarDepositoRequest gerarDepositoRequest,
                                         GerarAlvara27Request gerarAlvara27Request) {
        this.gerarDepositoRequest = gerarDepositoRequest;
        this.gerarAlvara27Request = gerarAlvara27Request;
    }

    public GerarDepositoRequest getGerarDepositoRequest() {
        return gerarDepositoRequest;
    }

    public void setGerarDepositoRequest(GerarDepositoRequest gerarDepositoRequest) {
        this.gerarDepositoRequest = gerarDepositoRequest;
    }

    public GerarAlvara27Request getGerarAlvara27Request() {
        return gerarAlvara27Request;
    }

    public void setGerarAlvara27Request(GerarAlvara27Request gerarAlvara27Request) {
        this.gerarAlvara27Request = gerarAlvara27Request;
    }

}
