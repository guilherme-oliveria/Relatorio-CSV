package br.jus.tjro.gabinete.service.remoto.sg;

import br.jus.tjro.gabinete.api.modelo.DocumentoApi;
import br.jus.tjro.gabinete.api.modelo.Movimento;
import br.jus.tjro.gabinete.api.modelo.PublicarDJE;
import br.jus.tjro.gabinete.api.modelo.VariaveisInstancia;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequisicaoPautaColegiadoRetornoConcluso {

    public static final String ID_RELATORIO = "73";
    public static final String ID_VOTO = "72";
    public static final String ID_EMENTA = "77";
    public static final String ID_ACORDAO = "74";
    public static final String ID_PAUTA = "63";
    private static final List<String> TIPO_DOCS = Arrays.asList(ID_RELATORIO,ID_VOTO,ID_EMENTA,ID_ACORDAO,ID_PAUTA);

    public RequisicaoPautaColegiadoRetornoConcluso(List<DocumentoApi> documentos, String idProcesso, String codigoSeguranca,
                                                   List<VariaveisInstancia> variaveisInstancia) throws Exception {
        List<String> currentDocs = documentos.stream().map(DocumentoApi::getTipoDocumento).toList();
        if(documentos.size() != 5)
            throw new Exception("Essa operação necessida de 5 documentos. Foi encontrado "+documentos.size());
        if(currentDocs.stream().filter(TIPO_DOCS::contains).count() != 5)
            throw new Exception("Um dos documentos "+currentDocs+" não é do tipo aceito");
        if(documentos.stream().anyMatch(it -> it.getDocumentoAssinado() == null))
            throw new Exception("Todos os documentos devem ter conteudo para envio ao pje");
        this.codigoSeguranca = codigoSeguranca;
        this.documentos = documentos;
        this.idProcesso = idProcesso;
        this.variaveis = variaveisInstancia;
    }

    private List<VariaveisInstancia> variaveis;

    private List<DocumentoApi> documentos;

    private PublicarDJE publicarDje;

    private boolean concluirImportacao = false;

    private boolean devolveSemManifestacao = false;

    private String idProcesso;

    private String codigoSeguranca;

    public List<DocumentoApi> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocumentoApi> documentos) {
        this.documentos = documentos;
    }

    public String getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(String idprocesso) {
        this.idProcesso = idprocesso;
    }

    public boolean isConcluirImportacao() {
        return concluirImportacao;
    }

    public void setConcluirImportacao(boolean concluirImportacao) {
        this.concluirImportacao = concluirImportacao;
    }

    public String getCodigoSeguranca() {
        return codigoSeguranca;
    }

    public void setCodigoSeguranca(String codigoSeguranca) {
        this.codigoSeguranca = codigoSeguranca;
    }

    public PublicarDJE getPublicarDje() {
        return publicarDje;
    }

    public void setPublicarDje(PublicarDJE publicarDje) {
        this.publicarDje = publicarDje;
    }

    public List<Movimento> getMovimentos() {
        return List.of(new Movimento("12311",List.of()));
    }

    public List<VariaveisInstancia> getVariaveis() {
        return variaveis;
    }

    public void setVariaveis(List<VariaveisInstancia> variaveis) {
        this.variaveis = variaveis;
    }

    public boolean isDevolveSemManifestacao() {
        return devolveSemManifestacao;
    }

    public void setDevolveSemManifestacao(boolean devolveSemManifestacao) {
        this.devolveSemManifestacao = devolveSemManifestacao;
    }

    public DocumentoApi getDocumentoApiByTipoDocumento(String tipoDocumento){
        List<DocumentoApi> lista = this.documentos.stream().filter(it -> tipoDocumento.equals(it.getTipoDocumento())).toList();
        if(lista.size() > 1)
            throw new RuntimeException("Existe mais de um documento para esse tipo de documento; id: "+tipoDocumento);
        if(lista.size() < 1)
            throw new RuntimeException("Não existe documento com o tipo de documento; id: "+tipoDocumento);
        return lista.get(0);
    }
}
