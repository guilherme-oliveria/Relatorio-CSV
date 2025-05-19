package br.jus.tjro.gabinete.model.gab.transiente;

import br.jus.tjro.gabinete.model.gab.enums.TipoNotificacaoEnum;

import java.util.Date;

public class Notificacao {
    TipoNotificacaoEnum tipo;
    Object payload;
    Date data;

    public Notificacao(TipoNotificacaoEnum tipo, Object payload, Date data) {
        this.tipo = tipo;
        this.payload = payload;
        this.data = data;
    }

    public TipoNotificacaoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacaoEnum tipo) {
        this.tipo = tipo;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
