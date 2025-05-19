package br.jus.tjro.gabinete.model.gab.minuta.modelos;

import br.jus.tjro.gabinete.model.gab.Usuario;

import java.util.*;
import java.util.stream.Collectors;

public class BuscaModelo {

    private final String cpf;
    private final String idTipoDocumento;
    private final String lotacaoAtual;
    private final String busca;
    private final List<String> idsLotacoes;
    private final List<String> tags;


    public BuscaModelo(String cpf, String lotacaoAtual, List<String> idsLotacoes) throws Exception {
        this(cpf,lotacaoAtual,idsLotacoes,null);
    }

    public BuscaModelo(String cpf, String lotacaoAtual, List<String> idsLotacoes,String busca) throws Exception {
        this(cpf,lotacaoAtual,idsLotacoes,busca,null, null);
    }

    public BuscaModelo(Usuario usuario,String lotacaoAtual) throws Exception {
        this(usuario.getCpf(),
            lotacaoAtual,
            usuario.getOrgaosJulgadores(),
            null,null,null);
    }

    public BuscaModelo(String cpf, String lotacaoAtual, List<String> orgaosJulgadoresAsString, String s, String idTipoDocumento) throws Exception {
        this(cpf,
            lotacaoAtual,
            orgaosJulgadoresAsString,
            s,idTipoDocumento,null);
    }

    public BuscaModelo(String cpf, String lotacaoAtual, List<String> idsLotacoes, String busca,String idTipoDocumento, List<String> tags) throws Exception {

        if(cpf == null)
            throw new Exception("O cpf não pode ser nulo");
        this.cpf = cpf;
        this.idTipoDocumento = idTipoDocumento;
        if(lotacaoAtual == null)
            throw new Exception("A Lotacao atual não pode ser nulo");
        this.lotacaoAtual = lotacaoAtual;
        this.busca = busca;
        if(idsLotacoes == null || idsLotacoes.size() == 0)
            throw new Exception("As Lotações não podem ser nulos ou vazias");
        this.idsLotacoes = idsLotacoes;
        if(tags != null)
            this.tags = tags;
        else
            this.tags = Collections.emptyList();
    }

    public HashMap<String, List<String>> getCpf() {
        return new HashMap<>(Map.of("cpf",List.of(cpf)));
    }

    public HashMap<String, List<String>> getIdTipoDocumento() {
        return idTipoDocumento != null && !idTipoDocumento.equals("") ? new HashMap<>(Map.of("idTipoDocumento",List.of(idTipoDocumento))) : null;
    }

    public HashMap<String, List<String>> getLotacaoAtual() {
        return new HashMap<>(Map.of("idOrgaoJulgador",List.of(lotacaoAtual)));
    }

    public HashMap<String, List<String>> getBusca() {
       return busca != null && !busca.equals("") ? new HashMap<>(Map.of("busca",List.of(busca))) : null;
    }

    public HashMap<String, List<String>> getIdsLotacoes() {
        return new HashMap<>(Map.of("idOrgaoJulgador",idsLotacoes != null ? idsLotacoes : new ArrayList<>()));
    }

    public List<String> getTags() {
        return tags != null ? tags : Collections.emptyList();
    }
}
