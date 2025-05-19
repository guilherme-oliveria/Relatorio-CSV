package br.jus.tjro.gabinete.repository.gab.filter;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.tpu.TpuClasse;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static com.google.common.base.Strings.isNullOrEmpty;

public class ProcessoFilter {

    private String numeroProcesso;

    private OrgaoJulgador orgaoJulgador;

    private TpuClasse classeJudicial;

    private String dataDe;

    private String dataAte;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataConclusoDe;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataConclusoAte;

    private List<Long> assuntos = new ArrayList<>();
    private List<Long> classes = new ArrayList<>();
    private List<Long> movimentos = new ArrayList<>();



    private List<Long> tags = new ArrayList<>();

    private String cpfCnpj;

    private String nomeDaParte;

    private int prioridadeProcessual;
    private String sentido;
    private int ordenacao;
    private long tipoDocumento;

    public Boolean isEmpty() {
       return isNullOrEmpty(numeroProcesso) && orgaoJulgador == null && classeJudicial == null && isNullOrEmpty(dataDe)
       && isNullOrEmpty(dataAte) && dataConclusoAte == null && dataConclusoDe == null && assuntos.isEmpty() && classes.isEmpty()
           && tags.isEmpty() && isNullOrEmpty(cpfCnpj) && isNullOrEmpty(nomeDaParte) && prioridadeProcessual == 0 && tipoDocumento == 0;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getNomeDaParte() {
        return nomeDaParte;
    }

    public void setNomeDaParte(String nomeDaParte) {
        this.nomeDaParte = nomeDaParte;
    }

    public long getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(long tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public int getPrioridadeProcessual() {
        return prioridadeProcessual;
    }

    public void setPrioridadeProcessual(int prioridadeProcessual) {
        this.prioridadeProcessual = prioridadeProcessual;
    }

    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public OrgaoJulgador getOrgaoJulgador() {
        return orgaoJulgador;
    }

    public void setOrgaoJulgador(OrgaoJulgador orgaoJulgador) {
        this.orgaoJulgador = orgaoJulgador;
    }

    public TpuClasse getClasseJudicial() {
        return classeJudicial;
    }

    public void setClasseJudicial(TpuClasse classeJudicial) {
        this.classeJudicial = classeJudicial;
    }

    public String getDataDe() {
        return dataDe;
    }

    public void setDataDe(String dataDe) {
        this.dataDe = dataDe;
    }

    public String getDataAte() {
        return dataAte;
    }

    public void setDataAte(String dataAte) {
        this.dataAte = dataAte;
    }

    public LocalDateTime getDataConclusoDe() {
        return dataConclusoDe;
    }

    public void setDataConclusoDe(LocalDateTime dataConclusoDe) {
        this.dataConclusoDe = dataConclusoDe;
    }

    public LocalDateTime getDataConclusoAte() {
        return dataConclusoAte;
    }

    public void setDataConclusoAte(LocalDateTime dataConclusoAte) {
        this.dataConclusoAte = dataConclusoAte;
    }

    public List<Long> getAssuntos() {
        return assuntos;
    }

    public void setAssuntos(List<Long> assuntos) {
        this.assuntos = assuntos;
    }

    public List<TpuClasse> getClasses() {
        return classes.stream().map(TpuClasse::new).collect(Collectors.toList());
    }

    public void setClasses(List<Long> classes) {
        this.classes = classes;
    }

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }

    public String getSentido() {
        return sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public int getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(int ordenacao) {
        this.ordenacao = ordenacao;
    }

    public List<Long> getMovimentos() {
        return movimentos;
    }

    public void setMovimentos(List<Long> movimentos) {
        this.movimentos = movimentos;
    }
}
