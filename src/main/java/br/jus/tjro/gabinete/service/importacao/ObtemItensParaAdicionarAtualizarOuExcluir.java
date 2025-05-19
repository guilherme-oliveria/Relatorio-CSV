package br.jus.tjro.gabinete.service.importacao;

import br.jus.tjro.gabinete.interfaces.importacao.EntidadeDoisBancos;

import java.util.*;

public class ObtemItensParaAdicionarAtualizarOuExcluir<T extends EntidadeDoisBancos, K extends EntidadeDoisBancos> {

    private final List<T> listaLocal;
    private final List<K> listaRemota;

    private final List<T> listaExclusao = new ArrayList<>();

    private final HashMap<T,K> listaAtualizacao = new HashMap<>();

    private final List<K> listaAdicao = new ArrayList<>();

    public List<T> getListaExclusao() {
        return listaExclusao;
    }

    public HashMap<T, K> getListaAtualizacao() {
        return listaAtualizacao;
    }

    public List<K> getListaAdicao() {
        return listaAdicao;
    }

    public ObtemItensParaAdicionarAtualizarOuExcluir(List<T> listaLocal, List<K> listaRemota){
        this.listaLocal = listaLocal;
        this.listaRemota = listaRemota;
        montaListaAdicao();
        montaListaExclusao();
        montaListaAtualizacao();
        if(listaAdicao.size()+listaAtualizacao.size() > listaRemota.size())
            throw new IllegalArgumentException("A implementação dos objetos de compração resulta em uma condição de adição e atualização invalida. " +
                "A soma de itens para modificar é maior que a os itens remoto");
    }

    private void montaListaAdicao() {
        listaRemota.forEach( r -> {
            EntidadeDoisBancos existeDadoLocal = null;
            if(listaLocal.size() > 0)
                existeDadoLocal = listaLocal.stream().filter(l -> l.getObjetoKeyString().equals(r.getObjetoKeyString())).findFirst().orElse(null);
            if(existeDadoLocal == null)
                listaAdicao.add(r);

        });
    }

    private void montaListaExclusao() {
        listaLocal.forEach( r -> {
            EntidadeDoisBancos existeDadoRemoto = null;
            if(listaRemota.size() > 0)
                existeDadoRemoto = listaRemota.stream().filter(l -> l.getObjetoKeyString().equals(r.getObjetoKeyString())).findFirst().orElse(null);
            if(existeDadoRemoto == null)
                listaExclusao.add(r);

        });
    }

    private void montaListaAtualizacao() {
        listaRemota.forEach( k -> {
            T existeDadoLocal = null;
            if(listaLocal.size() > 0)
                existeDadoLocal = listaLocal.stream().filter(l -> {
                        String localKeyId = l.getObjetoKeyString(),
                            localUpdateKey = l.getObjetoUpdateString(),
                            remoteKeyId = k.getObjetoKeyString(),
                            remoteUpdateKey = k.getObjetoUpdateString();
                        return (localKeyId.equals(remoteKeyId) && !localUpdateKey.equals(remoteUpdateKey));
                    }
                ).findFirst().orElse(null);
            if(existeDadoLocal != null)
                listaAtualizacao.put(existeDadoLocal,k);
        });
    }
}
