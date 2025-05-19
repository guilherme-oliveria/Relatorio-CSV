package br.jus.tjro.gabinete.model.gab.ia;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolicitacaoSugestaoTipoMovimento {
    private Map<String,String> mensagem;
    private Integer quantidadeClasses = 2;

    public SolicitacaoSugestaoTipoMovimento(String texto) {
        this.setMensagem(texto);
    }

    public SolicitacaoSugestaoTipoMovimento(String texto, Integer quantidadeClasses) {
        this.quantidadeClasses = quantidadeClasses;
        this.setMensagem(texto);
    }

    public void setMensagem(String texto) {
        this.mensagem = Map.of(
            "tipo", "DOCUMENTO",
            "conteudo", texto);
    }

    public Integer getQuantidadeClasses() {
        return quantidadeClasses;
    }

    public Map<String, String> getMensagem() {
        return mensagem;
    }
}
