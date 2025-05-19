package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.webjud.OrgaoEndereco;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class UsuarioJsonTest {


    @Test
    public void descerializaPapel() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Usuario resultado = mapper.reader().forType(Usuario.class).readValue(json);
    }


    @Test
    public void serializarPapel() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Usuario usuario = new Usuario("Windson","id",List.of(),"token");
        OrgaoEndereco endereco = new OrgaoEndereco("endereco","complemento","bairrro","municipio","numero","uf","sigla");
        Papel papel = new Papel("windson",List.of(new OrgaoJulgador("sistema-id","desc", List.of(endereco),"sigla", List.of())));
        usuario.setPapeis(Map.of("PJE",List.of(papel)));
        mapper.writeValueAsString(usuario);
    }

    public String json = "{\"nome\":\"Windson\",\"id\":\"id\",\"permissoes\":[]}";
}
