package br.jus.tjro.gabinete.scheduled.devolve.origem.builder;

import br.jus.tjro.gabinete.api.modelo.Movimento;
import br.jus.tjro.gabinete.api.modelo.MovimentoComplemento;
import br.jus.tjro.gabinete.api.modelo.TipoComplementoEnum;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimento;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaMovimentoComplemento;

import java.util.ArrayList;
import java.util.List;

public class MovimentoBuilderApi {

    public static List<Movimento> converteMovimentosMinutaParaMovimentosApi(List<MinutaMovimento> docMovs) {
        List<Movimento> movs = new ArrayList<Movimento>();
        docMovs.forEach(p -> movs.add(converteDocMovParaMov(p)));
        return movs;
    }

    private static Movimento converteDocMovParaMov(MinutaMovimento mov) {
        List<MovimentoComplemento> comps = converteDocMovCompParaMovComp(mov.getMinutaMovimentoComplementos());
        Movimento movimento = new Movimento(mov.getMovimentoId().toString(), comps);
        return movimento;
    }

    private static List<MovimentoComplemento> converteDocMovCompParaMovComp(List<MinutaMovimentoComplemento> comps) {
        List<MovimentoComplemento> docComps = new ArrayList<MovimentoComplemento>();
        for (MinutaMovimentoComplemento comp : comps) {
            switch (comp.getTipoComplementoEnum()) {
                case TABELADO:
                    docComps.add(new MovimentoComplemento(comp.getComplementoId().toString(), comp.getValor(), TipoComplementoEnum.DOMINIO));
                    break;
                case IDENTIFICADOR:
                    docComps.add(new MovimentoComplemento(comp.getComplementoId().toString(), comp.getValor(), TipoComplementoEnum.DINAMICO));
                    break;
                case LIVRE:
                    docComps.add(new MovimentoComplemento(comp.getComplementoId().toString(), comp.getValor(), TipoComplementoEnum.LIVRE));
                    break;
            }

        }
        return docComps;
    }

}
