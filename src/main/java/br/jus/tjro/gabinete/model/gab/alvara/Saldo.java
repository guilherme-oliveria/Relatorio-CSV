package br.jus.tjro.gabinete.model.gab.alvara;


import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

public class Saldo {
    private Double valor;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    public Saldo() {
    }

    public Saldo(Double valor, Date dataAtualizacao) {
        this.valor = valor;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
