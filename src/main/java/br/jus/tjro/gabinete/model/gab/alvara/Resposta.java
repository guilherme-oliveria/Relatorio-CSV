package br.jus.tjro.gabinete.model.gab.alvara;

public class Resposta {

    private boolean sucesso;
    private String mensagem;
    private Conteudo conteudo;

    public Resposta() {}

    public Resposta(boolean sucesso, String mensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }

    public Resposta(boolean sucesso, String mensagem, Conteudo conteudo) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.conteudo = conteudo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Conteudo getConteudo() {
        return conteudo;
    }

    public void setConteudo(Conteudo conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }
}
