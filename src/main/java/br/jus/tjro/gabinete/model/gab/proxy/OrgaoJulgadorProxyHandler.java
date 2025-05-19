package br.jus.tjro.gabinete.model.gab.proxy;

import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.service.local.OrgaoJulgadorService;

import java.lang.reflect.Method;

import static java.util.Optional.ofNullable;

public class OrgaoJulgadorProxyHandler implements ProxyableInvocationHandler<OrgaoJulgador> {

    private final LoogerInvocationHandler<OrgaoJulgador, String> handler;

    public OrgaoJulgadorProxyHandler(OrgaoJulgadorService service, String id){
        ofNullable(service).orElseThrow(() -> new IllegalArgumentException("O service não esta presente no proxy do Orgao Julgador"));

        this.handler = new LoogerInvocationHandler<>(id, OrgaoJulgador.class, String.class, (i) -> ofNullable(service.pegaOrgaoJulgadorPorId(i)));
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return handler.invoke(o, method, objects);
    }

    @Override
    public OrgaoJulgador criaProxy() {
        return handler.criaProxy();
    }

}
