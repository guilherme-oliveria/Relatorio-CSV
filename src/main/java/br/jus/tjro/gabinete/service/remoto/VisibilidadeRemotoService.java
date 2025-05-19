package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.interfaces.importacao.FonteDados;
import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.Visibilidade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

@Service
public class VisibilidadeRemotoService extends RemotoServiceAbstract {

    private final List<FonteDados> fontes;
    private final RestTemplate restTemplate;
    private final Logger looger = LoggerFactory.getLogger(VisibilidadeRemotoService.class);

    @Autowired
    public VisibilidadeRemotoService(List<FonteDados> fontes, RestTemplate restTemplate) {
        this.fontes = fontes;
        this.restTemplate = restTemplate;
    }

    private String getUrlBase(FonteDadosEnum fonte) {
        return getFonte(fonte).getUrlBase()+"/visibilidade";
    }

    private FonteDados getFonte(FonteDadosEnum fonte) {
        return fonte.selecionaFonte(fontes);
    }


    public void save(Processo processo, List<Long> idPessoaLegado) {
        String url = getUrlBase(processo.getFonteDados())+"/"+processo.getIdProcessoSistemaLegado();
        try {
            post(url,"",String.class,null);
        } catch (ServicoRemotoException e) {
            throw new RuntimeException(e);
        }
    }

    public void savePartes(Processo processo) {
        String url = getUrlBase(processo.getFonteDados())+"/partes/"+processo.getIdProcessoSistemaLegado();
        try {
            post(url,"",String.class,null);
        } catch (ServicoRemotoException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveOrgao(Processo processo) {
        String url = getUrlBase(processo.getFonteDados())+"/orgao/"+processo.getIdProcessoSistemaLegado();
        try {
            post(url,"",String.class,null);
        } catch (ServicoRemotoException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveOrgaoColegiado(Processo processo) {
        String url = getUrlBase(processo.getFonteDados())+"/orgao-colegiado/"+processo.getIdProcessoSistemaLegado();
        try {
            post(url,"",String.class,null);
        } catch (ServicoRemotoException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void delete(Processo processo, Long idVisibilidade) {
        String url = getUrlBase(processo.getFonteDados()) + "/"+idVisibilidade;
        try {
            exchange(url, HttpMethod.DELETE,new HttpEntity(""),String.class,null,false);
        } catch (ServicoRemotoException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Visibilidade> getByProcesso(Processo processo, Usuario usuario) throws ServicoRemotoException {
        Long idProcessoLegado = processo.getIdProcessoSistemaLegado();
        String url = getUrlBase(processo.getFonteDados());
        return Arrays.asList(Objects.requireNonNull(get(url + "/" + idProcessoLegado , Visibilidade[].class).getBody()));
    }

    public Boolean getIsAdmin(Usuario usuario) {
        String url = getUrlBase(FonteDadosEnum.PJEPG);
        RequestEntity<Void> request = RequestEntity.get(URI.create(url + "/is_admin/" + usuario.getId())).header("Authorization", usuario.getToken()).build();
        return restTemplate.exchange(request, Boolean.class).getBody();
    }


    public boolean temVisibilidade(Processo processo, Usuario usuario) {
        try{
            List<Visibilidade> visibilidades = getByProcesso(processo, usuario);
            return visibilidades.stream().anyMatch(it -> usuario.getCpf().equals(it.getNumerosCpf()));
        }catch (Throwable t){
            this.looger.error("Não foi possivel verificar se usuario tem visibilidade",t);
            return false;
        }
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }


}
