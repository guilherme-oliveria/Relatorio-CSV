package br.jus.tjro.gabinete.model.gab.proxy;

import org.springframework.cglib.proxy.InvocationHandler;

public interface ProxyableInvocationHandler<T> extends InvocationHandler {
    T criaProxy();
}
