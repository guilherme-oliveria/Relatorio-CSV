package br.jus.tjro.gabinete.exceptions;

public class ServicoRemotoException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public ServicoRemotoException(String string) {
        super(string);
    }
    
    public ServicoRemotoException(String string, Throwable e) {
        super(string,e);
    }

}
