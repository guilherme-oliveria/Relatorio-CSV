package br.jus.tjro.gabinete.exceptions;

public class StorageException extends Exception {

    private static final long serialVersionUID = -6793730068256016644L;

    public StorageException(String string) {
        super(string);
    }

    public StorageException(String string, Throwable e) {
        super(string,e);
    }

}
