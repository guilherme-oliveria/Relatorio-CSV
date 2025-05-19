package br.jus.tjro.gabinete.config.security;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PublicKeyConfig {

    @Autowired
    @Qualifier("customRestTemplate")
    private RestTemplate restTemplate;

    @Value("${keycloak.url-jwt:''}")
    private String UrlEmissorKeycloak;

    @Bean
    public String publicKey(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try{
            ResponseEntity responseEntity = restTemplate.getForEntity(UrlEmissorKeycloak,String.class);
            JSONObject json = new JSONObject(responseEntity.getBody().toString());

            StringBuilder value = new StringBuilder("-----BEGIN PUBLIC KEY-----\n");
            value.append(json.get("public_key").toString());
            value.append("\n-----END PUBLIC KEY-----");
            return value.toString();
        }catch (Exception e){
            return "";
        }
    }
}
