package br.jus.tjro.gabinete.scheduled.devolve.origem.builder;

import br.jus.tjro.gabinete.api.modelo.DocumentoApi;
import br.jus.tjro.gabinete.model.gab.minuta.Assinatura;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaAnexo;
import br.jus.tjro.gabinete.model.gab.minuta.PedidoInclusaoPauta;
import br.jus.tjro.gabinete.scheduled.devolve.origem.dto.AssinaturaApiDTO;
import br.jus.tjro.gabinete.scheduled.devolve.origem.dto.DocumentoApiDTO;
import br.jus.tjro.gabinete.service.assinaturadigital.RetornoAssinaturaService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentoBuilderApi {

    private static String formato = "yyyy-MM-dd'T'HH:mm:ss";

    private final RetornoAssinaturaService retornoAssinaturaService;

    public DocumentoBuilderApi(RetornoAssinaturaService retornoAssinaturaservice) {
        this.retornoAssinaturaService = retornoAssinaturaservice;
    }

    public DocumentoApiDTO parse(MinutaAnexo anexo) throws Exception {
        DocumentoApiDTO documentoApi = new DocumentoApiDTO();
        Minuta documentoPai = anexo.getMinutaPai();
        byte[] documentoAssinado = retornoAssinaturaService.converterParaAtachado(anexo);
        documentoApi.setContentType(anexo.getContentType());
        String descricao = anexo.getDescricao() == null ? anexo.getTipoDocumento().getDescricao() : anexo.getDescricao();
        documentoApi.setDescricao(descricao == null ? "Sem Descrição" : descricao);
        documentoApi.setDocumentoAssinado(documentoAssinado);
        documentoApi.setSigilo(anexo.isSigilo());
        documentoApi.setIdDocumentoPai(documentoPai.getIdMinutaSistemaLegado());
        documentoApi.setTipoDocumento(String.valueOf(anexo.getTipoDocumento().getId()));
        documentoApi.setIdModulo(getIdModulo(anexo.getId()));
        if(anexo.getAssinatura()!=null){
            documentoApi.setAssinaturaApiDTO(criarAssinaturaApi(anexo.getAssinatura()));
        }
        return documentoApi;
    }

    public DocumentoApiDTO parse(Minuta minuta) throws Exception {
        DocumentoApiDTO documentoApi = new DocumentoApiDTO();
        byte[] documentoAssinado = minuta.getArquivoAssinado();
        if (documentoAssinado == null)
            throw new Exception("O documento html com a assinatura esta null");
        documentoApi.setContentType("text/html");
        documentoApi.setDescricao(minuta.getTipoDocumento().getDescricao());
        documentoApi.setDocumentoAssinado(documentoAssinado);
        documentoApi.setSigilo(false);
        documentoApi.setTipoDocumento(minuta.getTipoDocumento().getId());
        documentoApi.setIdModulo(getIdModulo(minuta.getId()));
        if(minuta.getAssinatura()!=null){
            documentoApi.setAssinaturaApiDTO(criarAssinaturaApi(minuta.getAssinatura()));
        }
        return documentoApi;
    }

    private AssinaturaApiDTO criarAssinaturaApi(Assinatura assinatura){
        AssinaturaApiDTO assinaturaApiDTO = new AssinaturaApiDTO();
        assinaturaApiDTO.setAssinatura(assinatura.getAssinatura());
        assinaturaApiDTO.setCpfAssinou(assinatura.getCpfUsuario());
        assinaturaApiDTO.setDataAssinatura(converterDateParaString(assinatura.getDataAssinatura(), formato));
        assinaturaApiDTO.setCadeiaCertificado(assinatura.getCadeiaCertificado().getCadeiaCertificado());
        assinaturaApiDTO.setAlgoritmoDigest(assinatura.getAlgoritmoDigest());
        return assinaturaApiDTO;
    }

    private String getIdModulo(Long id) {
        String idStr = id == null ? "" : id.toString();
        return "gab-"+idStr;
    }

    public DocumentoApi parse(PedidoInclusaoPauta pauta) throws Exception {
        DocumentoApi documentoApi = new DocumentoApi();
        byte[] documentoAssinado = retornoAssinaturaService.converterParaAtachado(pauta);
        if (documentoAssinado == null)
            throw new Exception("O pedido de inclusão de pauta esta null");
        documentoApi.setContentType("text/html");
        documentoApi.setDescricao(pauta.getTipoDocumento().getDescricao());
        documentoApi.setDocumentoAssinado(documentoAssinado);
        documentoApi.setSigilo(false);
        documentoApi.setTipoDocumento(pauta.getTipoDocumento().getId());
        documentoApi.setIdModulo(getIdModulo(pauta.getId()));
        return documentoApi;
    }

    private static String converterDateParaString(Date date, String formato) {
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        return sdf.format(date);
    }
}
