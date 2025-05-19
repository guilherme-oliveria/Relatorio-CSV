package br.jus.tjro.gabinete.service.remoto;

import br.jus.tjro.gabinete.adapters.ModDocProcessoAdapter;
import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.infrastructure.filters.VariaveisFilter;
import br.jus.tjro.gabinete.interfaces.ModeloDocumentoService;
import br.jus.tjro.gabinete.model.docs.ModeloDocumentoEnv;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.BuscaModelo;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloDocumentoContador;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinutaContador;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador.AgrupadorModeloEnum;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinuta;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador.AgrupadorModeloManager;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.transiente.ModeloMinutaCabecalhoRodape;
import br.jus.tjro.gabinete.model.gab.transiente.VariavelTemplate.VariavelTemplate;
import br.jus.tjro.gabinete.repository.remoto.ModeloDocumentoRepository;
import br.jus.tjro.gabinete.repository.remoto.QueryModeloDocumento;

import br.jus.tjro.gabinete.service.local.OrgaoJulgadorService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static br.jus.tjro.gabinete.model.gab.minuta.modelos.agrupador.AgrupadorModeloEnum.*;

@Service
@Deprecated
public class ModeloDocumentoRemotoService extends RemotoServiceAbstract implements ModeloDocumentoService {

    private final RestTemplate restTemplate;
    private final String url;
    private final ModeloDocumentoRepository modeloDocumentoRepository;
    private final AgrupadorModeloManager agrupadorModeloManager;
    private final OrgaoJulgadorService orgaoJulgadorService;
    private final VariaveisFilter variaveisFilter;

    @Autowired
    ModeloDocumentoRemotoService(
        ModeloDocumentoEnv docsEnv,
        RestTemplate restTemplate,
        ModeloDocumentoRepository modeloDocumentoRepository,
        OrgaoJulgadorService orgaoJulgadorService,
        VariaveisFilter variaveisFilter) {
        this.modeloDocumentoRepository = modeloDocumentoRepository;
        this.agrupadorModeloManager = new AgrupadorModeloManager();
        this.orgaoJulgadorService = orgaoJulgadorService;
        this.url = docsEnv.getBackUrl();
        this.restTemplate = restTemplate;
        this.variaveisFilter = variaveisFilter;
    }

    @Override
    public Page<ModeloMinuta> filterPaginated(Pageable pageable, AgrupadorModeloEnum agrupadorEnum,
                                              BuscaModelo buscaModelo, Usuario usuario) throws ServicoRemotoException {
        QueryModeloDocumento query = new QueryModeloDocumento(agrupadorModeloManager.buscaQuery(buscaModelo,agrupadorEnum));
        return modeloDocumentoRepository.findBy(query,pageable,usuario);
    }

    @Override
    public ModeloMinuta copiarParaUsuario(String id, String idOrgaoJulgador, Usuario usuario) throws Exception {
        ModeloMinuta modelo = modeloDocumentoRepository.findById(id, usuario).orElseThrow(() -> new Exception("Modelo nao foi encontrado para copiar"));
        return modeloDocumentoRepository.save(new ModeloMinuta(modelo.getDescricao(), modelo.getTemplate(), idOrgaoJulgador, modelo.getIdTipoDocumento(), modelo.getTags()), usuario);
    }

    @Override
    public ModeloMinutaContador totais(BuscaModelo buscaModelo, Usuario usuario) throws Exception {
        List<HashMap<String, List<String>>> requisicao = List.of(
            agrupadorModeloManager.buscaQuery(buscaModelo, MEUS),
            agrupadorModeloManager.buscaQuery(buscaModelo, TJ),
            agrupadorModeloManager.buscaQuery(buscaModelo, TODASVARAS),
            agrupadorModeloManager.buscaQuery(buscaModelo, VARA)
        );

        return new ModeloMinutaContador(modeloDocumentoRepository.totais(requisicao
            .stream()
            .map(QueryModeloDocumento::new)
            .collect(Collectors.toList()), usuario));
    }

    @Override
    public List<ModeloDocumentoContador> totaisDocumento(BuscaModelo buscaModelo, Usuario usuario) throws Exception {
        List<HashMap<String, List<String>>> requisicao = List.of(
            agrupadorModeloManager.buscaQuery(buscaModelo, MEUS),
            agrupadorModeloManager.buscaQuery(buscaModelo, TJ),
            agrupadorModeloManager.buscaQuery(buscaModelo, TODASVARAS),
            agrupadorModeloManager.buscaQuery(buscaModelo, VARA)
        );

        var querys = requisicao.stream().map(QueryModeloDocumento::new).collect(Collectors.toList());
        Map<String, Long> resultado = modeloDocumentoRepository.totais(querys, usuario);
        if(resultado != null)
        return resultado
            .entrySet()
            .stream()
            .map(t -> new ModeloDocumentoContador(AgrupadorModeloEnum.getEnum(t.getKey()), t.getValue()))
            .collect(Collectors.toList());
        else
            return null;
    }


    @Override
    @Retryable
    public String renderizarVariavel(Usuario usuario, Processo processo, String template, Boolean assinando) throws ServicoRemotoException {
        var processoAdapter = new ModDocProcessoAdapter(processo, orgaoJulgadorService, usuario);
        var variaveis = Jsoup.parse(template)
            .select("[data-tag]").stream()
            .map(el -> el.attr("data-tag"))
            .distinct()
            .collect(Collectors.toList());
        var dados = processoAdapter.adapt(variaveis,assinando);
        return post(url + "/render", Map.of("template", template,"dados", dados), String.class, null, MediaType.APPLICATION_JSON).getBody();
    }

    @Recover
    public String renderizaFallBack(Throwable t, Usuario usuario, Processo processo, String template, Boolean assinando) throws Exception {
        if(assinando)
            throw new Exception("O serviço de renderização de modelos de documento esta indisponivel, não é possivel assinar no momento",t);
        return template;
    }

    @Override
    public String renderizarVariavel(Usuario usuario, Processo processo, String template) throws ServicoRemotoException {
       return this.renderizarVariavel(usuario,processo,template,false);
    }

    @Override
    public ModeloMinutaCabecalhoRodape getCabecalhoERodape() {
        return null;
    }

    @Override
    @Cacheable(value = "variaveisModeloDocumento")
    public List<VariavelTemplate> getVariaveisTemplate(String token) throws ServicoRemotoException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> request = new HttpEntity<String>(headers);
        VariavelTemplate[] arrVar = restTemplate.exchange(url + "/variaveis", HttpMethod.GET, request, VariavelTemplate[].class).getBody();
        List<VariavelTemplate> variaveis = Arrays.asList(arrVar);

        return (variaveis != null) ?
            variaveis.stream()
                .filter(variavel -> !variaveisFilter.chaveIsExclude(variavel))
                .filter(variavel -> !variaveisFilter.grupoIsExclude(variavel))
                .collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public Optional<ModeloMinuta> findById(String id, Usuario usuario) throws Exception {
        return modeloDocumentoRepository.findById(id, usuario);
    }

    @Override
    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
