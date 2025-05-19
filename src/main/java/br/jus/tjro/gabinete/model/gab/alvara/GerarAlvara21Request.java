package br.jus.tjro.gabinete.model.gab.alvara;

import br.jus.tjro.gabinete.model.gab.processo.Processo;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

public class GerarAlvara21Request {

    private String nomeBeneficiario;
    private String documentoBeneficiario;
    private String contaOrigemId;
    private String codBancoOrigem;
    private String agenciaOrigem;
    private String numeroContaOrigem;
    private String digitoVerificadorOrigem;
    private String idDeposito;

    private Long orgaoId;
    private String numeroProcesso;
    private TipoPessoa naturezaSacador;
    private String nomeSacador;
    private String documentoSacador;
    private BigDecimal valorAPagar;
    private FinalidadePagamento finalidadePagamento;
    private RepresentacaoProcessual representacaoProcessual;
    private String nomeAdvogadoReclamante;
    private String oabAdvogadoReclamante;
    private String numeroDocumentoAdvogadoReclamante;
    private TipoCredito tipoCredito;
    private String codBancoDestino;
    private String agenciaDestino;
    private String numeroContaDestino;
    private String digitoVerificadorDestino;
    private TipoConta tipoConta;

    private String dataValidade;
    private String dataAtualizacao;
    private Integer codComarca;
    private TipoPessoa naturezaSacador2;
    private String nomeSacador2;
    private String documentoSacador2;
    private BigDecimal baseCalculoIR;
    private BigDecimal valorIR;
    private String observacoes;

    private Requisitante requisitante;
    private Requisitante primeiroRevisor;
    private Requisitante segundoRevisor;
    private Requisitante autorizador;

    public String idOrigem;

    public GerarAlvara21Request() {
    }

    public GerarAlvara21Request(PagamentoAlvara pagamentoAlvara, Long orgaoJulgadorDepara, Processo processo, Requisitante requisitante, Requisitante autorizador, int validadeAlvara, LocalDateTime dataCriacao) {
        idOrigem = pagamentoAlvara.getId().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        nomeBeneficiario = pagamentoAlvara.getNomeFavorecido();
        documentoBeneficiario = pagamentoAlvara.getDocFavorecido();
        contaOrigemId = pagamentoAlvara.getContaJudicial().getCodLegado();
        codBancoOrigem = pagamentoAlvara.getContaJudicial().getCodBanco();
        agenciaOrigem = pagamentoAlvara.getContaJudicial().getAgencia();
        numeroContaOrigem = pagamentoAlvara.getContaJudicial().getNumeroConta();
        digitoVerificadorOrigem = pagamentoAlvara.getContaJudicial().getDigitoVerificador();
        orgaoId = orgaoJulgadorDepara;
        if(pagamentoAlvara.getValidade() == null) pagamentoAlvara.setValidade(validadeAlvara);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, pagamentoAlvara.getValidade());
        this.dataValidade = dateFormat.format(c.getTime());
        if(pagamentoAlvara.getComAtualizacao()){
            this.dataAtualizacao = DateTimeFormatter.ofPattern("yyyyMMdd").format(dataCriacao);
        }
        numeroProcesso =  processo.numeroProcessoSemFormatacao();
        if (pagamentoAlvara.getDocFavorecido().length() > 11) {
            naturezaSacador = TipoPessoa.PESSOA_JURIDICA;
        } else {
            naturezaSacador = TipoPessoa.PESSOA_FISICA;
        }
        nomeSacador = pagamentoAlvara.getNomeFavorecido();
        documentoSacador = pagamentoAlvara.getDocFavorecido();
        representacaoProcessual = RepresentacaoProcessual.JUS_POSTULANDI;

        if (pagamentoAlvara.getFormaPagamento().equals("T")) {
            tipoCredito = TipoCredito.EM_CONTA;
            codBancoDestino = pagamentoAlvara.getConta().getBanco();
            agenciaDestino = pagamentoAlvara.getConta().getAgencia();
            numeroContaDestino = pagamentoAlvara.getConta().getNumeroConta();
            digitoVerificadorDestino = pagamentoAlvara.getConta().getDigitoVerificador();
            tipoConta = TipoConta.getTIpoContaPeloCodigo(pagamentoAlvara.getConta().getOperacao());
        } else if (pagamentoAlvara.getFormaPagamento().equals("A")) {
            if(pagamentoAlvara.getNomeSacador() == null || pagamentoAlvara.getNomeSacador().isBlank()) {
                nomeSacador = pagamentoAlvara.getNomeFavorecido();
                documentoSacador = pagamentoAlvara.getDocFavorecido();
            }else {
                nomeSacador = pagamentoAlvara.getNomeSacador();
                documentoSacador = pagamentoAlvara.getDocumentoSacador();
            }
            nomeSacador2 = pagamentoAlvara.getNomeSacador2();
            documentoSacador2 = pagamentoAlvara.getDocumentoSacador2();
            tipoCredito = TipoCredito.EM_ESPECIE;
        }

        valorAPagar = pagamentoAlvara.getValor();
        this.requisitante = requisitante;
        primeiroRevisor  = requisitante;
        segundoRevisor = requisitante;
        this.autorizador = autorizador;
    }

    public String getNomeBeneficiario() {
        return nomeBeneficiario;
    }

    public void setNomeBeneficiario(String nomeBeneficiario) {
        this.nomeBeneficiario = nomeBeneficiario;
    }

    public String getDocumentoBeneficiario() {
        return documentoBeneficiario;
    }

    public void setDocumentoBeneficiario(String documentoBeneficiario) {
        this.documentoBeneficiario = documentoBeneficiario;
    }

    public String getCodBancoOrigem() {
        return codBancoOrigem;
    }

    public void setCodBancoOrigem(String codBancoOrigem) {
        this.codBancoOrigem = codBancoOrigem;
    }

    public String getAgenciaOrigem() {
        return agenciaOrigem;
    }

    public void setAgenciaOrigem(String agenciaOrigem) {
        this.agenciaOrigem = agenciaOrigem;
    }

    public String getNumeroContaOrigem() {
        return numeroContaOrigem;
    }

    public void setNumeroContaOrigem(String numeroContaOrigem) {
        this.numeroContaOrigem = numeroContaOrigem;
    }

    public String getDigitoVerificadorOrigem() {
        return digitoVerificadorOrigem;
    }

    public void setDigitoVerificadorOrigem(String digitoVerificadorOrigem) {
        this.digitoVerificadorOrigem = digitoVerificadorOrigem;
    }

    public String getIdDeposito() {
        return idDeposito;
    }

    public void setIdDeposito(String idDeposito) {
        this.idDeposito = idDeposito;
    }

    public Long getOrgaoId() {
        return orgaoId;
    }

    public void setOrgaoId(Long orgaoId) {
        this.orgaoId = orgaoId;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public TipoPessoa getNaturezaSacador() {
        return naturezaSacador;
    }

    public void setNaturezaSacador(TipoPessoa naturezaSacador) {
        this.naturezaSacador = naturezaSacador;
    }

    public String getNomeSacador() {
        return nomeSacador;
    }

    public void setNomeSacador(String nomeSacador) {
        this.nomeSacador = nomeSacador;
    }

    public String getDocumentoSacador() {
        return documentoSacador;
    }

    public void setDocumentoSacador(String documentoSacador) {
        this.documentoSacador = documentoSacador;
    }

    public BigDecimal getValorAPagar() {
        return valorAPagar;
    }

    public void setValorAPagar(BigDecimal valorAPagar) {
        this.valorAPagar = valorAPagar;
    }

    public FinalidadePagamento getFinalidadePagamento() {
        return finalidadePagamento;
    }

    public void setFinalidadePagamento(FinalidadePagamento finalidadePagamento) {
        this.finalidadePagamento = finalidadePagamento;
    }

    public RepresentacaoProcessual getRepresentacaoProcessual() {
        return representacaoProcessual;
    }

    public void setRepresentacaoProcessual(RepresentacaoProcessual representacaoProcessual) {
        this.representacaoProcessual = representacaoProcessual;
    }

    public String getNomeAdvogadoReclamante() {
        return nomeAdvogadoReclamante;
    }

    public void setNomeAdvogadoReclamante(String nomeAdvogadoReclamante) {
        this.nomeAdvogadoReclamante = nomeAdvogadoReclamante;
    }

    public String getOabAdvogadoReclamante() {
        return oabAdvogadoReclamante;
    }

    public void setOabAdvogadoReclamante(String oabAdvogadoReclamante) {
        this.oabAdvogadoReclamante = oabAdvogadoReclamante;
    }

    public String getNumeroDocumentoAdvogadoReclamante() {
        return numeroDocumentoAdvogadoReclamante;
    }

    public void setNumeroDocumentoAdvogadoReclamante(String numeroDocumentoAdvogadoReclamante) {
        this.numeroDocumentoAdvogadoReclamante = numeroDocumentoAdvogadoReclamante;
    }

    public TipoCredito getTipoCredito() {
        return tipoCredito;
    }

    public void setTipoCredito(TipoCredito tipoCredito) {
        this.tipoCredito = tipoCredito;
    }

    public String getCodBancoDestino() {
        return codBancoDestino;
    }

    public void setCodBancoDestino(String codBancoDestino) {
        this.codBancoDestino = codBancoDestino;
    }

    public String getAgenciaDestino() {
        return agenciaDestino;
    }

    public void setAgenciaDestino(String agenciaDestino) {
        this.agenciaDestino = agenciaDestino;
    }

    public String getNumeroContaDestino() {
        return numeroContaDestino;
    }

    public void setNumeroContaDestino(String numeroContaDestino) {
        this.numeroContaDestino = numeroContaDestino;
    }

    public String getDigitoVerificadorDestino() {
        return digitoVerificadorDestino;
    }

    public void setDigitoVerificadorDestino(String digitoVerificadorDestino) {
        this.digitoVerificadorDestino = digitoVerificadorDestino;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public String getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(String dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Integer getCodComarca() {
        return codComarca;
    }

    public void setCodComarca(Integer codComarca) {
        this.codComarca = codComarca;
    }

    public TipoPessoa getNaturezaSacador2() {
        return naturezaSacador2;
    }

    public void setNaturezaSacador2(TipoPessoa naturezaSacador2) {
        this.naturezaSacador2 = naturezaSacador2;
    }

    public String getNomeSacador2() {
        return nomeSacador2;
    }

    public void setNomeSacador2(String nomeSacador2) {
        this.nomeSacador2 = nomeSacador2;
    }

    public String getDocumentoSacador2() {
        return documentoSacador2;
    }

    public void setDocumentoSacador2(String documentoSacador2) {
        this.documentoSacador2 = documentoSacador2;
    }

    public BigDecimal getBaseCalculoIR() {
        return baseCalculoIR;
    }

    public void setBaseCalculoIR(BigDecimal baseCalculoIR) {
        this.baseCalculoIR = baseCalculoIR;
    }

    public BigDecimal getValorIR() {
        return valorIR;
    }

    public void setValorIR(BigDecimal valorIR) {
        this.valorIR = valorIR;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Requisitante getRequisitante() {
        return requisitante;
    }

    public void setRequisitante(Requisitante requisitante) {
        this.requisitante = requisitante;
    }

    public Requisitante getPrimeiroRevisor() {
        return primeiroRevisor;
    }

    public void setPrimeiroRevisor(Requisitante primeiroRevisor) {
        this.primeiroRevisor = primeiroRevisor;
    }

    public Requisitante getSegundoRevisor() {
        return segundoRevisor;
    }

    public void setSegundoRevisor(Requisitante segundoRevisor) {
        this.segundoRevisor = segundoRevisor;
    }

    public Requisitante getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(Requisitante autorizador) {
        this.autorizador = autorizador;
    }

    public String getContaOrigemId() {
        return contaOrigemId;
    }

    public void setContaOrigemId(String contaOrigemId) {
        this.contaOrigemId = contaOrigemId;
    }
}
