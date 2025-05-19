package br.jus.tjro.gabinete.service.local.tpu;

import br.jus.tjro.gabinete.model.gab.tpu.TpuAssunto;
import br.jus.tjro.gabinete.model.gab.tpu.TpuComplemento;
import br.jus.tjro.gabinete.model.gab.tpu.TpuMovimento;
import br.jus.tjro.gabinete.service.remoto.tpu.TpuMovimentoRemotoService;
import br.jus.tjro.gabinete.util.ParametrosUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TpuMovimentoService {

    private final TpuMovimentoRemotoService tpuMovimentoRemotoService;

    @Autowired
    public TpuMovimentoService(TpuMovimentoRemotoService tpuMovimentoRemotoService) {
        this.tpuMovimentoRemotoService = tpuMovimentoRemotoService;

    }

    @Deprecated
    public List<TpuMovimento> getMovimentos(List<Long> codigos) throws Exception {
        return tpuMovimentoRemotoService.getMovimentos(codigos);
    }

    @Deprecated
    public TpuMovimento getMovimento(Long codigo) {
        return tpuMovimentoRemotoService.getMovimento(codigo);
    }

    @Deprecated
    public TpuMovimento getMovimentoComComplementos(Long codigo) {
        return tpuMovimentoRemotoService.getMovimentoComComplementos(codigo);
    }

    @Deprecated
    public List<TpuMovimento> getArvoreMovimentos(Long id) throws Exception {
        return tpuMovimentoRemotoService.getArvoreMovimentos(id);
    }

    // Complementos
    @Deprecated
    public List<TpuComplemento> getComplementosDoMovimento(Long codigoMovimento) {
        return tpuMovimentoRemotoService.getComplementosDoMovimento(codigoMovimento);
    }

    @Deprecated
    public TpuComplemento getComplemento(Long codigoComplemento) {
        return tpuMovimentoRemotoService.getComplemento(codigoComplemento);
    }

    @Deprecated
    public List<TpuMovimento> getMovimentosComComplemento(List<Long> codigos) throws Exception {
        return tpuMovimentoRemotoService.getMovimentosComComplemento(codigos);
    }

}
