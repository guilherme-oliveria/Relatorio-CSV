package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.exceptions.ServicoRemotoException;
import br.jus.tjro.gabinete.interfaces.ModeloDocumentoService;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.PedidoInclusaoPauta;
import br.jus.tjro.gabinete.model.gab.minuta.modelos.ModeloMinuta;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.repository.gab.PedidoInclusaoPautaRepository;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasAnexoRepository;
import br.jus.tjro.gabinete.repository.gab.minuta.MinutasRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Service
public class PedidoInclusaoPautaService {
    private final ModeloDocumentoService modeloDocumento;
    private final PedidoInclusaoPautaRepository pedidoInclusaoPautaRepository;
    private final MinutasRepository minutasRepository;
    private final MinutasAnexoRepository minutasAnexoRepository;
    private final String template;
    private final String templateId;

    private final Logger looger = LoggerFactory.getLogger(PedidoInclusaoPautaService.class);

    @Autowired
    public PedidoInclusaoPautaService(ModeloDocumentoService modeloDocumento,
                                      PedidoInclusaoPautaRepository pedidoInclusaoPautaRepository,
                                      MinutasRepository minutasRepository,
                                      MinutasAnexoRepository minutasAnexoRepository,
                                      @Value("${pedido.inclusao.pauta.template:}") String template,
                                      @Value("${pedido.inclusao.pauta.mod-doc-template-id:}") String templateId) {
        this.modeloDocumento = modeloDocumento;
        this.pedidoInclusaoPautaRepository = pedidoInclusaoPautaRepository;
        this.template = template;
        this.templateId = templateId;
        this.minutasAnexoRepository = minutasAnexoRepository;
        this.minutasRepository = minutasRepository;
    }



    public Minuta talvezGeraPauta(Minuta minuta, Usuario usuario) throws Exception {
        if(minuta.getTipoDocumento().isMinutaColegiado() && minuta.getPedidoInclusaoPauta().isEmpty()){
            PedidoInclusaoPauta pedidoInclusaoPauta = pedidoInclusaoPautaRepository.findByMinuta(minuta)
                .orElse(new PedidoInclusaoPauta(minuta));
            pedidoInclusaoPauta.setHtmlRenderizado(resolveTemplate());
            pedidoInclusaoPauta.renderizarHtml(modeloDocumento, usuario, minuta.getProcesso(), false);
            minuta = pedidoInclusaoPautaRepository.save(pedidoInclusaoPauta).getMinuta();
        }
        return minuta;
    }

    public String resolveTemplate() {
        if(this.templateId.isBlank()) {
            return this.template;
        } else {
            try {
                Optional<ModeloMinuta> byId = modeloDocumento.findById(this.templateId, null);
                return byId.map(ModeloMinuta::getTemplate)
                    .orElse("Não foi possível localizar o template " + this.templateId);
            } catch (Exception exception) {
                looger.error("Erro ao localizar o template " + this.templateId);
                return this.template;
            }
        }
    }

    public PedidoInclusaoPauta render(PedidoInclusaoPauta pauta, Usuario usuario, boolean assinando) throws ServicoRemotoException {
        pauta.setHtmlRenderizado(resolveTemplate());
        pauta.renderizarHtml(modeloDocumento, usuario, pauta.getMinuta().getProcesso(), assinando);
        return pedidoInclusaoPautaRepository.save(pauta);
    }
}
