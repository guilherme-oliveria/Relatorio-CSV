package br.jus.tjro.gabinete.util;

import br.jus.tjro.gabinete.model.gab.tpu.TpuComplemento;
import br.jus.tjro.gabinete.model.gab.transiente.OpcaoComplemento;
import br.jus.tjro.gabinete.model.gab.transiente.TipoComplemento;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TpuComplementoDeserializer extends JsonDeserializer<TpuComplemento> {

    public static TpuComplemento buildItem(JsonNode node) {
        final Long codigo = node.get("codigo").asLong();
        final String descricao = node.get("descricao").asText();
        final String observacao = node.get("observacao").asText();
        final String valor = node.has("valor") ? node.get("valor").asText() : ""; //node.getMap("valores").elements().forEachRemaining(e -> {return e.asText()}) : "";

        String tipoComplKey = "tipo_complemento";
        String opcoesKey = "complementos_tabelados";
        if (node.has("tipoComplemento")) {
            tipoComplKey = "tipoComplemento";
            opcoesKey = "opcoes";
        }
        JsonNode nodeTpC = node.get(tipoComplKey);
        TipoComplemento tipoComplemento = new TipoComplemento(nodeTpC.get("id").asLong(), nodeTpC.get("nome").asText(), nodeTpC.get("observacao").asText());

        List<OpcaoComplemento> opcoes = new ArrayList<>();
        Iterator<JsonNode> opts = node.get(opcoesKey).elements();
        opts.forEachRemaining(jsonNode -> {
            opcoes.add(new OpcaoComplemento(jsonNode.get("codigo").asText(), jsonNode.get("descricao").asText()));
        });

        TpuComplemento complemento = new TpuComplemento(codigo, descricao, observacao);
        complemento.setTipoComplemento(tipoComplemento);
        complemento.setOpcoes(opcoes);
        complemento.setValor(valor);

        return complemento;
    }

    @Override
    public TpuComplemento deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ObjectCodec oc = parser.getCodec();
        JsonNode node = oc.readTree(parser);
        return TpuComplementoDeserializer.buildItem(node);
    }
}
