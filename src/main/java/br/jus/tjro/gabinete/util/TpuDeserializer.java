package br.jus.tjro.gabinete.util;

import br.jus.tjro.gabinete.interfaces.TpuInterface;
import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.model.gab.tpu.TpuComplemento;
import br.jus.tjro.gabinete.model.gab.tpu.TpuMovimento;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TpuDeserializer extends JsonDeserializer<TpuInterface> {

    protected JsonParser jsonParser;
    protected DeserializationContext deserializationContext;

    @Override
    public TpuInterface deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        this.deserializationContext = ctxt;
        this.jsonParser = jp;
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        TpuInterface tpu = buildItem(node, null);
        return tpu;
    }

    private TpuInterface buildItem(JsonNode node, TpuInterface pai) {
        TpuInterface tpu = null;

        final Long codigo = node.has("codigo") ? node.get("codigo").asLong() : null;
        final Long codigoPai = node.has("seq_elemento_pai") ? node.get("seq_elemento_pai").asLong() : null;
        final String descricao = node.has("descricao") ? node.get("descricao").asText() : null;
        final String glossario = node.has("glossario") ? node.get("glossario").asText() : null;
        final String situacao = node.has("situacao") ? node.get("situacao").asText() : null;
        final Boolean temFilhos = node.has("filhos") ? node.get("filhos").asBoolean() : null;

        if (node.has("assunto_id") || (node.has("resourceName") && Objects.equals(node.get("resourceName").asText(), "assuntos"))) {
            tpu = new TpuAssunto(codigo, descricao, glossario, codigoPai, situacao, temFilhos);
        } else if (node.has("movimento_id") || (node.has("resourceName") && Objects.equals(node.get("resourceName").asText(), "movimentos"))) {
            tpu = new TpuMovimento(codigo, descricao, glossario, codigoPai, situacao, temFilhos);

            if (node.has("complementos")) {
                ((TpuMovimento) tpu).setComplementos(buildComplementos(node.get("complementos")));
            }

        } else if (node.has("classe_id") || (node.has("resourceName") && Objects.equals(node.get("resourceName").asText(), "classes"))) {
            tpu = new TpuClasse(codigo, descricao, glossario, codigoPai, situacao, temFilhos);
        }

        if (tpu == null)
            return null;

        if (pai != null)
            tpu.getBreadcrumb().addAll(pai.getBreadcrumb());
        tpu.getBreadcrumb().add(codigo);

        if (node.has("arvore_filhos")) {
            List<TpuInterface> filhos = new ArrayList<>();
            TpuInterface finalTpu = tpu;
            node.get("arvore_filhos").elements().forEachRemaining(jsonNode -> {
                TpuInterface newItem = buildItem(jsonNode, finalTpu);
                filhos.add(newItem);
            });
            tpu.setFilhos(filhos);
        }
        return tpu;
    }

    private List<TpuComplemento> buildComplementos(JsonNode complementos) {
        List<TpuComplemento> lista = new ArrayList<>();
        complementos.elements().forEachRemaining(jnode -> {
            lista.add(TpuComplementoDeserializer.buildItem(jnode));
        });
        return lista;
    }
}
