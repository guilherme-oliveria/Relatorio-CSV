package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.dto.UsuarioMobileDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UsuarioRemotoService extends RemotoServiceAbstract {

    @Value("${URL_PJE_PG_REST:http://localhost:8281}")
    private String urlServicoPG;

    private final RestTemplate restTemplate;

    public UsuarioRemotoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<UsuarioMobileDTO> getDispositivosMobile(String cpfLogin) throws Exception {
        String url = urlServicoPG + "/mobile/dispositivos-pareados/" + cpfLogin.replaceAll("[^0-9]", "");
        RequestEntity<Void> request = get(url, "Erro ao buscar dispositivos mobile");
        UsuarioMobileDTO[]  mobileRemotoArray = restTemplate.exchange(request, UsuarioMobileDTO[].class).getBody();
        List<UsuarioMobileDTO> mobiRemoto = mobileRemotoArray != null ? Arrays.asList(mobileRemotoArray) : Arrays.asList();
        return mobiRemoto;
    }


    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
