package br.jus.tjro.gabinete.exceptions.service.local;

public class ProcessoDocumentoServiceException extends Exception {

    public ProcessoDocumentoServiceException(String string) {
        super(string);
    }

    public ProcessoDocumentoServiceException(String s, Exception e) {
        super(s,e);
    }
}
