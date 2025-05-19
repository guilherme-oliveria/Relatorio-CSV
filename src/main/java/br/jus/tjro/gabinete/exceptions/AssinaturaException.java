package br.jus.tjro.gabinete.exceptions;

public class AssinaturaException extends Exception {

    private static final long serialVersionUID = -6793730068256016644L;

    public AssinaturaException(String string) {
        super(string);
    }

    public AssinaturaException(String string,Exception t) {
        super(string,t);
    }

}
