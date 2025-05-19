package br.jus.tjro.gabinete.exceptions;

public class TagDeSistemaException extends Exception {

    private static final long serialVersionUID = -6793730068256016644L;

    public TagDeSistemaException() {
        super("Esta é uma tag reservada ao sistema e não pode ser excluída");
    }

    public TagDeSistemaException(String string) {
        super(string);
    }

    public TagDeSistemaException(String string,Exception t) {
        super(string,t);
    }

}
