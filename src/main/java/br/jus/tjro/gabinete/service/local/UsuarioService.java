package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.dto.AuthUserLogoutDTO;
import br.jus.tjro.gabinete.dto.UsuarioMobileDTO;
import br.jus.tjro.gabinete.scheduled.BuscaEnderecosDasPartesDosProcessos;
import br.jus.tjro.gabinete.service.remoto.UsuarioRemotoService;
import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class UsuarioService {

    private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    @Qualifier("customRestTemplate")
    private RestTemplate restTemplate;
    private UsuarioRemotoService usuarioRemotoService;

    private final String url;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String uriLogout;

    private static final Integer tamanhoSenha = 6;
    private Base32 base32 = new Base32();
    private TimeBasedOneTimePasswordGenerator otpGenerator;

    public UsuarioService(@Value("${keycloak.auth-server-url}") String url,
                             @Value("${keycloak.client-id}") String clientId,
                             @Value("${keycloak.client-secret}") String clientSecret,
                             @Value("${keycloak.redirect-uri}") String redirectUri,
                             @Value("${keycloak.auth-server-url-logout}") String uriLogout,
                             UsuarioRemotoService usuarioRemotoService) {
        this.url = Objects.requireNonNull(url, "url não pode ser nulo");
        this.clientId = Objects.requireNonNull(clientId, "clientId não pode ser nulo");
        this.clientSecret = Objects.requireNonNull(clientSecret, "clientSecret não pode ser nulo");
        this.redirectUri = Objects.requireNonNull(redirectUri, "redirectUri não pode ser nulo");
        this.uriLogout = Objects.requireNonNull(uriLogout, "uriLogout não pode ser nulo");
        this.usuarioRemotoService = usuarioRemotoService;
    }

    public String login(String code) throws Exception{
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("code", code);
            requestBody.add("grant_type", "authorization_code");
            requestBody.add("client_id", clientId);
            requestBody.add("client_secret", clientSecret);
            requestBody.add("redirect_uri", redirectUri);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            System.out.println("client_id: " + clientId);
            System.out.println("client_secret: " + clientSecret);
            System.out.println("redirect_uri: " + redirectUri);
            return null;
        }
    }

    public Boolean logout(String requestBody) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        AuthUserLogoutDTO authUserLogout = mapper.readValue(requestBody, AuthUserLogoutDTO.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + authUserLogout.getAccessToken());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", authUserLogout.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(uriLogout, requestEntity, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT)
            return true;
        else
            throw new Exception("Erro ao realizar logout. Status: " + responseEntity.getStatusCodeValue());
    }

    public String refreshToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("refresh_token", token);
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        return  responseEntity.getBody();
    }

    public Boolean validarToken(String token, String cpf) {
        Instant data = Instant.now();
        try {
            List<UsuarioMobileDTO> listMobile = usuarioRemotoService.getDispositivosMobile(cpf);

            for (UsuarioMobileDTO usuarioMobile : listMobile) {
                String tokenAtual = gerarTokenTempo(usuarioMobile.getCodigoPareamento(), data);

                if (tokenAtual.equals(token)) {
                    return true;
                }
            }
            logger.error("Nenhum token correspondeu ao token fornecido: ", token);
            return false;
        }catch (Exception e){
            logger.error("Erro ao validar TOKEN MOBILE: "+cpf , e);
            return false;
        }
    }


    public String gerarTokenTempo(String codigoPareamento, Instant data) throws Exception {
        SecretKey key = new SecretKeySpec(base32.decode(codigoPareamento), "SHA1");
        return StringUtils.leftPad(getOTPGenerator().generateOneTimePassword(key, data)+"", tamanhoSenha, '0');
    }

    private TimeBasedOneTimePasswordGenerator getOTPGenerator () throws NoSuchAlgorithmException {
        if ( otpGenerator==null ) {
            otpGenerator = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30), tamanhoSenha, "HmacSHA1");
        }
        return otpGenerator;
    }
}
