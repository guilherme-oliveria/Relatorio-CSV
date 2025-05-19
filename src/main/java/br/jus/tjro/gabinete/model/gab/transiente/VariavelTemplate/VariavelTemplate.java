package br.jus.tjro.gabinete.model.gab.transiente.VariavelTemplate;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;


public class VariavelTemplate {

    private final Logger looger = LoggerFactory.getLogger(VariavelTemplate.class);

    private Long id;
    private String chave;
    private String grupo;
    private String descricao;
    private String valor;
    @Deprecated
    @JsonIgnore
    private Boolean renderizavel = true;
    private Boolean exibirNoFront = true;
    private List<ParametroVariavelTemplate> parametros;


    @JsonIgnore
    public void setValoresParametros(String variavelParametros) throws UnsupportedEncodingException {
        if(!variavelParametros.isEmpty()) {
            variavelParametros = URLDecoder.decode(variavelParametros, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> parametros = new HashMap<>();
            try {
                parametros = mapper.readValue(String.valueOf(variavelParametros), new TypeReference<HashMap<String,String>>() {});
                HashMap<String, String> finalParametros = parametros;
                this.parametros.stream().forEach(p -> {
                    if(finalParametros.containsKey(p.getChave())){
                        p.setValor(finalParametros.get(p.getChave()));
                    }else{
                        p.setValorPadrao();
                    }
                });
            } catch (IOException e) {
                looger.error(String.format("Não foi possível setar os parametros da variavel %s", this.chave), e);
            }
        } else {
            this.parametros.forEach(ParametroVariavelTemplate::setValorPadrao);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Boolean getRenderizavel() {
        return renderizavel;
    }

    public void setRenderizavel(Boolean renderizavel) {
        this.renderizavel = renderizavel;
    }

    public Boolean getExibirNoFront() {
        return exibirNoFront;
    }

    public void setExibirNoFront(Boolean exibirNoFront) {
        this.exibirNoFront = exibirNoFront;
    }

    public List<ParametroVariavelTemplate> getParametros() {
        return parametros;
    }

    public void setParametros(List<ParametroVariavelTemplate> parametros) {
        this.parametros = parametros;
    }
}
