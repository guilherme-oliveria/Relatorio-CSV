package br.jus.tjro.gabinete.exceptions;

public class DevolveOrigemException extends Exception {

    private static final long serialVersionUID = -6793730068256016644L;

    public DevolveOrigemException(String string) {
        super(string);
    }

    public DevolveOrigemException(String s, Exception e) {
        super(s+". Causa: "+e.getMessage(),e);
    }
}
