package br.jus.tjro.gabinete.model.gab.assinaturamobile;

import java.util.List;

public class RespostaAssinatura {
    private String status;
    private Integer code;
    private List<String> messages;
    private ResultadoAssinatura data;

    public RespostaAssinatura() {
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public ResultadoAssinatura getData() {
        return this.data;
    }

    public void setData(ResultadoAssinatura data) {
        this.data = data;
    }
}
