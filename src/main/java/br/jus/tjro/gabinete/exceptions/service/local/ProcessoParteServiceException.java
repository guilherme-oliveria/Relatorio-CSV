package br.jus.tjro.gabinete.exceptions.service.local;

public class ProcessoParteServiceException extends Exception {

    public ProcessoParteServiceException(String string) {
        super(string);
    }

    public ProcessoParteServiceException(String s, Exception e) {
        super(s,e);
    }
}
