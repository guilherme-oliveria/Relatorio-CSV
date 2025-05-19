package br.jus.tjro.gabinete.model.gab;

import br.jus.tjro.gabinete.model.Assinavel;

import java.util.ArrayList;
import java.util.List;

public class ParametrosEnvioArquivosParaAssinaturaTjOffice {

    private String nome;
    private String url;
    private List<String> paramsEnvio = new ArrayList<String>();

    public ParametrosEnvioArquivosParaAssinaturaTjOffice(String nome, String url, Assinavel documento) {
        this.nome = nome;
        this.url = url;
        this.paramsEnvio = documento.getParametrosAssinatura();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getParamsEnvio() {
        return paramsEnvio;
    }

    public void setParamsEnvio(List<String> paramsEnvio) {
        this.paramsEnvio = paramsEnvio;
    }
}
