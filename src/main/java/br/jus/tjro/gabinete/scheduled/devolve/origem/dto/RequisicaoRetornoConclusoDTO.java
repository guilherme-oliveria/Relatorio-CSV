package br.jus.tjro.gabinete.scheduled.devolve.origem.dto;

import br.jus.tjro.gabinete.api.RequisicaoRetornoConcluso;

public class RequisicaoRetornoConclusoDTO extends RequisicaoRetornoConcluso {

    private boolean lancarMovimentacaoSemManifestar = false;

    public RequisicaoRetornoConclusoDTO(String idProcesso, String codigoSeguranca, Boolean devolveSemManifestacao, boolean lancarMovimentacaoSemManifestar) {
        super(idProcesso, codigoSeguranca, devolveSemManifestacao);
        this.lancarMovimentacaoSemManifestar = lancarMovimentacaoSemManifestar;
    }

    public boolean isLancarMovimentacaoSemManifestar() {
        return lancarMovimentacaoSemManifestar;
    }

    public void setLancarMovimentacaoSemManifestar(boolean lancarMovimentacaoSemManifestar) {
        this.lancarMovimentacaoSemManifestar = lancarMovimentacaoSemManifestar;
    }
}
