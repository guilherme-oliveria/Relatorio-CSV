package br.jus.tjro.gabinete.model.gab.proxy;

import br.jus.tjro.gabinete.model.gab.TipoDocumento;
import br.jus.tjro.gabinete.service.local.TipoDocumentoService;

import java.lang.reflect.Method;

import static java.util.Optional.ofNullable;

public class TipoDocumentoProxyHandler implements ProxyableInvocationHandler<TipoDocumento> {

    private final LoogerInvocationHandler<TipoDocumento, String> handler;

    public TipoDocumentoProxyHandler(TipoDocumentoService service, String id){
        ofNullable(service).orElseThrow(() -> new IllegalArgumentException("O service não esta presente no proxy do Tipo Documento"));

        this.handler = new LoogerInvocationHandler<>(id, TipoDocumento.class, String.class, service::findById);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        return handler.invoke(o, method, args);
    }

    @Override
    public TipoDocumento criaProxy() {
        return handler.criaProxy();
    }

}
