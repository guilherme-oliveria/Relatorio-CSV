package br.jus.tjro.gabinete.model.gab.proxy;

import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.service.local.tpu.TpuAssuntoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.Arrays;

public class TpuAssuntoProxyHandler implements InvocationHandler {

    private final Logger log = LoggerFactory.getLogger(TpuAssuntoProxyHandler.class);

    private final TpuAssuntoService service;
    private final Long id;
    private TpuAssunto tpuAssunto;
    private final TpuAssunto tpuAssuntoProxy;
    private final String[] metodosProxyaveis = {"getCodigo","equals"};

    public TpuAssuntoProxyHandler(TpuAssuntoService service, Long id){
        if(service == null)
            throw new IllegalArgumentException("O service não esta presente no proxy do tpuClasse");
        this.service = service;
        this.id = id;
        this.tpuAssuntoProxy = new TpuAssunto(id);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        TpuAssunto retorno;
        if (tpuAssunto == null && Arrays.asList(metodosProxyaveis).contains(method.getName())) {
            return method.invoke(this.tpuAssuntoProxy, args);
        }
        else if (this.tpuAssunto == null) {
            try{
                this.tpuAssunto = service.getAssunto(id);
                if(this.tpuAssunto == null)
                    this.tpuAssunto = new TpuAssunto(id,id+" Serviço da TPU esta Indisponivel","",null,"",false);
            }catch (Throwable t){
                log.error("Erro ao carregar assunto. Causa: "+t.getMessage(),t);
                this.tpuAssunto = new TpuAssunto(id,id+" Serviço da TPU esta Indisponivel","",null,"",false);
            }finally {
                retorno = this.tpuAssunto;
            }
        }else{
            retorno = tpuAssunto;
        }
        return method.invoke(retorno, args);
    }

    public TpuAssunto criaProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(this);
        enhancer.setSuperclass(TpuAssunto.class);
        return (TpuAssunto) enhancer.create(new Class[] {Long.class}, new Object[] {id});
    }
}
