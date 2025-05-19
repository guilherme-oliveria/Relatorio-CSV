package br.jus.tjro.gabinete.service.remoto.status.servicos;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.model.gab.transiente.status.servicos.StatusServicoResposta;
import br.jus.tjro.gabinete.service.remoto.RemotoServiceAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StatusServicosService extends RemotoServiceAbstract
{

    @Value("${URL_PJE_SG_REST:http://localhost:8282}")
    private String urlServicoSG;

    @Value("${URL_PJE_PG_REST:http://localhost:8281}")
    private String urlServicoPG;

    private final RestTemplate restTemplate;

    @Autowired
    public StatusServicosService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public StatusServicoResposta statusPjePGWebService() throws ServicoRemotoException {
        return statusPje(urlServicoPG);
    }

    public StatusServicoResposta statusPjeSGWebService() throws ServicoRemotoException {
        return statusPje(urlServicoSG);
    }

    private StatusServicoResposta statusPje(String urlServico) throws ServicoRemotoException {
        String metricas = "";
        try{
            metricas = restTemplate.exchange(urlServico + "/prometheus", HttpMethod.GET, null, String.class).getBody();
        } catch (Exception ex) {
            metricas = "";
        }
        try {
            StatusServicoResposta resp = restTemplate.exchange(urlServico + "/health", HttpMethod.GET, null, StatusServicoResposta.class).getBody();
            resp.setMetricas(this.processarMetricas(metricas));
            return resp;
        } catch (Exception e) {
            StatusServicoResposta resp = new StatusServicoResposta();
            resp.status = "DOWN";
            return resp;
        }
    }

    private Map<String,String> processarMetricas(String inputMetricas) {
        Map<String,String> metricas = new HashMap<>();
        String pattern = "^(?!.*#)(\\w*)(\\s)(.*)$";
        Pattern regex = Pattern.compile(pattern, Pattern.MULTILINE);
        Matcher matcher = regex.matcher(inputMetricas);

        while(matcher.find()) {
            metricas.put(matcher.group(1), matcher.group(3));
        }

        return metricas;
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
