package br.jus.tjro.gabinete.model.gab.alvara;

public class GerarAlvaraRequest {
    private Requisitante requisitante;
    private Requisitante primeiroRevisor;
    private Requisitante segundoRevisor;
    private Requisitante autorizador;


    public Requisitante getRequisitante() {
        return requisitante;
    }

    public void setRequisitante(Requisitante requisitante) {
        this.requisitante = requisitante;
    }

    public Requisitante getPrimeiroRevisor() {
        return primeiroRevisor;
    }

    public void setPrimeiroRevisor(Requisitante primeiroRevisor) {
        this.primeiroRevisor = primeiroRevisor;
    }

    public Requisitante getSegundoRevisor() {
        return segundoRevisor;
    }

    public void setSegundoRevisor(Requisitante segundoRevisor) {
        this.segundoRevisor = segundoRevisor;
    }

    public Requisitante getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(Requisitante autorizador) {
        this.autorizador = autorizador;
    }

}
