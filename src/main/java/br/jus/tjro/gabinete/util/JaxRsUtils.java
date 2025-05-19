package br.jus.tjro.gabinete.util;

import org.jboss.resteasy.client.jaxrs.BasicAuthentication;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

public class JaxRsUtils {
    public static <T> T criarProxyDoServico(Class<T> service, String path) {
        return criarProxyDoServico(service, path, null, null);
    }

    public static <T> T criarProxyDoServico(Class<T> service, String path, String user, String password) {
        try {
            ResteasyClient client = new ResteasyClientBuilder().build();
            ResteasyWebTarget target = client.target(path);
            if (user != null && password != null) {
                target.register(new BasicAuthentication(user, password));
            }
            return target.proxy(service);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao instanciar o proxy do serviço " + service.getClass().getSimpleName()
                + ", mensagem interna: " + e.getMessage(), e);
        }
    }
}
