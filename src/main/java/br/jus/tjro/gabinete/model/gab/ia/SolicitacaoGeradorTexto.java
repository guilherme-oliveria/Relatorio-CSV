package br.jus.tjro.gabinete.model.gab.ia;


public class SolicitacaoGeradorTexto {

    public SolicitacaoGeradorTexto(String texto, Integer quantidadeRamificacoes, Integer quantidadePalavras) {
        this.texto = texto;
        this.quantidadeRamificacoes = quantidadeRamificacoes;
        this.quantidadePalavras = quantidadePalavras;
    }


    private String texto;
    private Integer quantidadeRamificacoes;
    private Integer quantidadePalavras;

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Integer getQuantidadeRamificacoes() {
        return quantidadeRamificacoes;
    }

    public void setQuantidadeRamificacoes(Integer quantidadeRamificacoes) {
        this.quantidadeRamificacoes = quantidadeRamificacoes;
    }

    public Integer getQuantidadePalavras() {
        return quantidadePalavras;
    }

    public void setQuantidadePalavras(Integer quantidadePalavras) {
        this.quantidadePalavras = quantidadePalavras;
    }
}
