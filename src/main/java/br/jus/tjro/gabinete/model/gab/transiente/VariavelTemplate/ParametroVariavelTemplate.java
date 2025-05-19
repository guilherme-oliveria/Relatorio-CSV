package br.jus.tjro.gabinete.model.gab.transiente.VariavelTemplate;


import java.util.List;

public class ParametroVariavelTemplate {
    private String descricao;
    private String chave;
    private List<OpcaoVariavelTemplate> opcoes;
    private String valor;

    public void setValorPadrao() {
        this.valor = this.getOpcoes().stream()
            .filter(OpcaoVariavelTemplate::getPadrao)
            .map(OpcaoVariavelTemplate::getValor)
            .findAny().orElse("");

    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public List<OpcaoVariavelTemplate> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<OpcaoVariavelTemplate> opcoes) {
        this.opcoes = opcoes;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
