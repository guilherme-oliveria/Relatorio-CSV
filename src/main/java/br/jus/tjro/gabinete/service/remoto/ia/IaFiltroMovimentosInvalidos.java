package br.jus.tjro.gabinete.service.remoto.ia;

import br.jus.tjro.gabinete.model.gab.tpu.TpuMovimento;
import br.jus.tjro.gabinete.service.local.tpu.TpuMovimentoService;
import br.jus.tjro.sinapses.api.modelo.ClassificacaoClasseConviccao;
import br.jus.tjro.sinapses.api.modelo.ClassificacaoResultado;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IaFiltroMovimentosInvalidos {

    private TpuMovimentoService tpuMovimentoService;

    public IaFiltroMovimentosInvalidos() {}

    @Autowired
    public IaFiltroMovimentosInvalidos(TpuMovimentoService tpuMovimentoService) {
        this.tpuMovimentoService = tpuMovimentoService;
    }

    public ClassificacaoResultado filtrar(ClassificacaoResultado classificacaoResultado) {
        List<Long> codigos = classificacaoResultado.getResultados().stream()
            .map(iaMov -> Long.parseLong(iaMov.getClasse().getCodigo()))
            .collect(Collectors.toList());

        List<TpuMovimento> movimentos = null;
        try {
            movimentos = tpuMovimentoService.getMovimentos(codigos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TpuMovimento> finalMovimentos = movimentos;
        List<ClassificacaoClasseConviccao> filtrados = classificacaoResultado.getResultados().stream().filter(classificacaoClasseConviccao -> {
            Long codigo = Long.parseLong(classificacaoClasseConviccao.getClasse().getCodigo());
            Optional<TpuMovimento> movimento = finalMovimentos.stream().filter(tpuMovimento -> tpuMovimento.getCodigo().equals(codigo)).findFirst();
            return movimento.isPresent() && movimento.get().ativo() && !movimento.get().getTemFilhos();
        }).collect(Collectors.toList());

        classificacaoResultado.setResultados(filtrados);

        return classificacaoResultado;
    }



}
