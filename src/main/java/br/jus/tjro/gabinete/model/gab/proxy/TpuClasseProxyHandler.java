package br.jus.tjro.gabinete.model.gab.proxy;

import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.service.local.tpu.TpuClasseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

// ToDo: Usar ProxyableInvocationHandler
public class TpuClasseProxyHandler implements InvocationHandler {

    private final Logger log = LoggerFactory.getLogger(TpuClasseProxyHandler.class);

    private final TpuClasseService service;
    private final Long id;
    private TpuClasse tpuClasse;
    private final TpuClasse tpuClasseProxy;
    private final String[] metodosProxyaveis = {"getCodigo","equals"};

    public TpuClasseProxyHandler(TpuClasseService service, Long id){
        if(service == null)
            throw new IllegalArgumentException("O service não esta presente no proxy do tpuClasse");
        this.service = service;
        this.id = id;
        this.tpuClasseProxy = new TpuClasse(id);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        TpuClasse retorno;
        if (tpuClasse == null && Arrays.asList(metodosProxyaveis).contains(method.getName())) {
            return method.invoke(this.tpuClasseProxy, args);
        }
        else if (this.tpuClasse == null) {
            try{
                this.tpuClasse = service.getClasse(id);
                retorno = this.tpuClasse;
            }catch (Throwable t){
                log.error("Erro ao carregar classe. Causa: "+t.getMessage(),t);
                retorno = tpuClasseProxy;
            }
        }else{
            retorno = tpuClasse;
        }
        return method.invoke(retorno, args);
    }

    public TpuClasse criaProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(this);
        enhancer.setSuperclass(TpuClasse.class);
        return (TpuClasse) enhancer.create(new Class[] {Long.class}, new Object[] {id});
    }
}
