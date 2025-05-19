package br.jus.tjro.gabinete.model.gab.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Arrays.asList;

public class LoogerInvocationHandler<T, E> implements ProxyableInvocationHandler<T> {

    private final Logger log = LoggerFactory.getLogger(LoogerInvocationHandler.class);

    private final E id;
    private  T proxy;
    private final Class<E> idClazz;
    private final Function<E, Optional<T>> serviceResolve;
    private final Class<T> clazz;
    private T object;
    private final String[] proxiableMethods = {"getId","equals"};

    public LoogerInvocationHandler(E id, Class<T> clazz, Class<E> idClazz, Function<E, Optional<T>> serviceResolve) {
        this.id = id;
        this.idClazz = idClazz;
        this.serviceResolve = serviceResolve;
        try {
            this.proxy = clazz.getDeclaredConstructor(idClazz).newInstance(id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        T retorno;
        if (this.object == null && asList(proxiableMethods).contains(method.getName())) {
            return method.invoke(this.proxy, args);
        }
        else if (this.object == null) {
            try {
                this.object = serviceResolve.apply(id).orElseThrow(NullPointerException::new);
                retorno = this.object;
            }catch (Throwable t){
                log.info(String.format("Erro ao carregar %s. Causa: %s ", clazz.getSimpleName(), t.getMessage()));
                retorno = proxy;
            }
        } else {
            retorno = object;
        }
        return method.invoke(retorno, args);
    }

    @Override
    public T criaProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(this);
        enhancer.setSuperclass(clazz);
        return (T) enhancer.create(new Class[] {idClazz}, new Object[] {id});
    }
}
