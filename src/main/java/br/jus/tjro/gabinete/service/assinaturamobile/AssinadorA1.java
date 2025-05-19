package br.jus.tjro.gabinete.service.assinaturamobile;

import br.jus.tjro.gabinete.model.gab.assinaturamobile.RespostaAssinatura;
import br.jus.tjro.gabinete.model.gab.assinaturamobile.ResultadoAssinatura;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class AssinadorA1 {

    private final org.slf4j.Logger looger = LoggerFactory.getLogger(AssinadorA1.class);


    private String urlAssinador;

    @Autowired
    @Qualifier("customRestTemplate")
    private RestTemplate restTemplate;

    public AssinadorA1(@Value("${url.api.assinador.mobile:''}") String url){
        this.urlAssinador = url;
    }

    public ResultadoAssinatura assinarHash(String hash) throws Exception {

        try {
            String apiUrl = urlAssinador + hash;

            ResponseEntity<RespostaAssinatura> responseEntity = restTemplate.getForEntity(apiUrl, RespostaAssinatura.class);
            RespostaAssinatura resp = responseEntity.getBody();

            if ("ok".equals(resp.getStatus())) {
                return resp.getData();
            } else {
                throw new Exception(resp.getMessages().get(0).toString());
            }
        } catch (Exception var6) {
            looger.error("Erro ao chamar serviço de assinatura",var6 );
            throw new Exception("Erro ao chamar serviço de assinatura", var6);
        }
    }
}
