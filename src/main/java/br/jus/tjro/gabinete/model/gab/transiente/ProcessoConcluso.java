package br.jus.tjro.gabinete.model.gab.transiente;

import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@Deprecated
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessoConcluso {

    private Long idProcessoLegado;

    private String numeroProcesso;

    private String instancia;

    private boolean justicaGratuita;

    private String motivoSegredoJustica;

    private boolean possuiLiminar;

    private boolean segredoJustica;

    private boolean digital;

    private String orgaoJulgador;

    private String classeJudicial;

    private String assuntoPrincipal;

    private Date dataUltimaDistribuicao;

    private Double valorCausa;

    private FonteDadosEnum fonteDadosEnum;

    private String competencia;

    private Integer[] listaIdsPrioridades;

    private int nivelAcesso;

    private Long idColegiado;

    private String modeloDocumentoHtmlPje;

    private String tiposDocumentos;


    public ProcessoConcluso() {
    }

    public ProcessoConcluso(
        Long idProcessoLegado, String numeroProcesso, String instancia,
        boolean justicaGratuita, boolean possuiLiminar, boolean segredoJustica,
        String orgaoJulgador, String classeJudicial, Date dataUltimaDistribuicao,
        Double valorCausa, String competencia, boolean digital, int nivelAcesso, Long idColegiado,
        String motivoSegredoJustica
    ) {
        super();
        this.idProcessoLegado = idProcessoLegado;
        this.numeroProcesso = numeroProcesso;
        this.instancia = instancia;
        this.justicaGratuita = justicaGratuita;
        this.possuiLiminar = possuiLiminar;
        this.segredoJustica = segredoJustica;
        this.orgaoJulgador = orgaoJulgador;
        this.classeJudicial = classeJudicial;
        this.dataUltimaDistribuicao = dataUltimaDistribuicao;
        this.valorCausa = valorCausa;
        this.competencia = competencia;
        this.digital = digital;
        this.nivelAcesso = nivelAcesso;
        this.idColegiado = idColegiado;
        this.motivoSegredoJustica = motivoSegredoJustica;
    }

    public Long getIdProcessoLegado() {
        return idProcessoLegado;
    }

    public void setIdProcessoLegado(Long idProcessoLegado) {
        this.idProcessoLegado = idProcessoLegado;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public String getInstancia() {
        return instancia;
    }

    public void setInstancia(String instancia) {
        this.instancia = instancia;
    }

    public boolean isJusticaGratuita() {
        return justicaGratuita;
    }

    public void setJusticaGratuita(boolean justicaGratuita) {
        this.justicaGratuita = justicaGratuita;
    }

    public boolean isPossuiLiminar() {
        return possuiLiminar;
    }

    public void setPossuiLiminar(boolean possuiLiminar) {
        this.possuiLiminar = possuiLiminar;
    }

    public boolean isSegredoJustica() {
        return segredoJustica;
    }

    public void setSegredoJustica(boolean segredoJustica) {
        this.segredoJustica = segredoJustica;
    }

    public String getMotivoSegredoJustica() {
        return motivoSegredoJustica;
    }

    public void setMotivoSegredoJustica(String motivoSegredoJustica) {
        this.motivoSegredoJustica = motivoSegredoJustica;
    }

    public String getClasseJudicial() {
        return classeJudicial;
    }

    public void setClasseJudicial(String classeJudicial) {
        this.classeJudicial = classeJudicial;
    }

    public String getOrgaoJulgador() {
        return orgaoJulgador;
    }

    public void setOrgaoJulgador(String orgaoJulgador) {
        this.orgaoJulgador = orgaoJulgador;
    }

    public String getAssuntoPrincipal() {
        return assuntoPrincipal;
    }

    public void setAssuntoPrincipal(String assuntoPrincipal) {
        this.assuntoPrincipal = assuntoPrincipal;
    }

    public Double getValorCausa() {
        return valorCausa;
    }

    public void setValorCausa(Double valorCausa) {
        this.valorCausa = valorCausa;
    }

    public Date getDataUltimaDistribuicao() {
        return dataUltimaDistribuicao;
    }

    public void setDataUltimaDistribuicao(Date dataUltimaDistribuicao) {
        this.dataUltimaDistribuicao = dataUltimaDistribuicao;
    }

    public FonteDadosEnum getFonteDadosEnum() {
        return fonteDadosEnum;
    }

    public void setFonteDadosEnum(FonteDadosEnum fonteDadosEnum) {
        this.fonteDadosEnum = fonteDadosEnum;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public Integer[] getListaIdsPrioridades() {
        return listaIdsPrioridades;
    }

    public void setListaIdsPrioridades(Integer[] listaIdsPrioridades) {
        this.listaIdsPrioridades = listaIdsPrioridades;
    }

    public Boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }

    public int getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(int nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    public Long getIdColegiado() {
        return idColegiado;
    }

    public void setIdColegiado(Long idColegiado) {
        this.idColegiado = idColegiado;
    }

    public String getModeloDocumentoHtmlPje() {
        return modeloDocumentoHtmlPje;
    }

    public void setModeloDocumentoHtmlPje(String modeloDocumentoHtmlPje) {
        this.modeloDocumentoHtmlPje = modeloDocumentoHtmlPje;
    }

    public String getTiposDocumentos() {
        return tiposDocumentos;
    }

    public void setTiposDocumentos(String tiposDocumentos) {
        this.tiposDocumentos = tiposDocumentos;
    }

}
