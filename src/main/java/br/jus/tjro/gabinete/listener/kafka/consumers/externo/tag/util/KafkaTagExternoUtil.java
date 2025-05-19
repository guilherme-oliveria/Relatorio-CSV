package br.jus.tjro.gabinete.listener.kafka.consumers.externo.tag.util;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoTag;
import br.jus.tjro.gabinete.model.gab.processo.WrapperTagExterna;
import br.jus.tjro.gabinete.service.local.ProcessoService;
import br.jus.tjro.gabinete.service.local.ProcessoTagService;
import br.jus.tjro.gabinete.service.local.localizador.LocalizadorService;
import org.slf4j.Logger;

import java.util.Optional;

public class KafkaTagExternoUtil {

    public static void extrairTagElocalizadorParaProcesso(WrapperTagExterna wtc, ProcessoService processoService,
                                                          Logger logger, ProcessoTagService processoTagService,
                                                          LocalizadorService localizadorService, String sistema)
        throws Exception {
        try {
            Optional<Processo> processo =
                processoService.findByIdProcessoSistemaLegadoAndSistemaOrNumeroProcessoAndSistema(
                    wtc.getProcessoLegadoId(), wtc.getNumeroProcesso(), wtc.getFonteDados());
            if(processo.isPresent()) {
                String orgaoJulgador = processo.get().getOrgaoJulgador();
                Long idTag = null;
                try {
                    logger.info("Gerando tag '" + wtc.getTag() + "' a partir do " + sistema + ". " +
                        "Numero Processo: " + processo.get().getNumeroProcesso());
                    ProcessoTag pt = processoTagService.salvarTagUsandoWrapperExterno(wtc, processo.get());
                    idTag = pt.getTag().getId();
                    logger.info("Finalizando tag '" + wtc.getTag() + "' do " + sistema + " para o processo: "
                        + processo.get().getNumeroProcesso());
                } catch (Exception e) {
                    logger.error("Erro ao processar tag '" + wtc.getTag() + "' do " + sistema + " para o processo: "
                        + processo.get().getNumeroProcesso());
                }
                try {
                    logger.info("Verificando se existe algum localizador da tag '" + wtc.getTag() + "' no OJ: "
                        + orgaoJulgador);
                    localizadorService.verificaSeExisteLocalizadorCaixaPorNomeContendoEOrgaoJulgadorSeNaoCria(
                        wtc.getTag(), processo.get(), idTag);
                    logger.info("Finalizando a geração do localizador da tag '" + wtc.getTag() + "' para o OJ: "
                        + orgaoJulgador);
                } catch (Exception e) {
                    logger.error("Erro ao tentar gerar/buscar o localizador da tag '" + wtc.getTag() + "' para/no OJ: "
                        + orgaoJulgador);
                }
            }
        } catch (Exception e) {
            throw new Exception("Erro ao processar mensagem da fila da tag '" + wtc.getTag() + "' do " + sistema + " " +
                "para id processo legado: " + wtc.getProcessoLegadoId() + "-" + wtc.getFonteDados().fonte
                + " . Erro: " + e, e);
        }
    }


}
