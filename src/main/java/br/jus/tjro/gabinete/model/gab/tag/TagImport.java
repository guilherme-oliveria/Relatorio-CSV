package br.jus.tjro.gabinete.model.gab.tag;

import br.jus.tjro.gabinete.model.gab.minuta.Anotacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "tag_import")
@SequenceGenerator(name = TagImport.SEQUENCE_NAME, sequenceName = TagImport.SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
public class TagImport {

    public static final String SEQUENCE_NAME = "SEQUENCIA_TAG_IMPORT";

    public static final int TIPO_INCLUIR = 100;
    public static final int TIPO_ATUALIZAR = 101;
    public static final int TIPO_EXCLUIR= 102;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @Column(name = "ID")
    private Long id;
    @Column(name = "nr_oab")
    private String nrOab;
    @Column(name = "sigla_estado", length = 3)
    private String siglaEstado;
    @Column(name = "nr_processo")
    private String nrProcesso;
    @Column(name = "nome_tag")
    @NotNull
    private String nomeTag;
    @Column(name = "cor")
    private String cor;
    @Column(name = "url")
    private String url;
    @Column(name = "aviso")
    private String aviso;
    @Column(name = "apenas_meu_gabinete")
    private Boolean apenasMeuGabinete;

    @Transient
    private int countLinha =0;
    @Transient
    private List<String> listNomeTag;

    public TagImport() {
    }

    public TagImport(String nrProcesso,String nomeTag, String cor, String aviso, String url, String apenasMeuGabinete, int countLinha) {
        this.nrProcesso = nrProcesso;
        this.nomeTag=nomeTag;
        if(cor!=null)
            this.cor = cor;
        else
            this.cor = "#5bc0de";

        this.url = url;
        this.aviso = aviso;
        if(apenasMeuGabinete!=null)
            this.apenasMeuGabinete = convertSimNaoToBoolean(apenasMeuGabinete);
        else
            this.apenasMeuGabinete = false;

        this.countLinha=countLinha;
    }

    public TagImport(String nrOab,String siglaEstado, String nomeTag, String cor, String aviso, String url, String apenasMeuGabinete, int countLinha) {
        this.nrOab = nrOab;
        this.siglaEstado = siglaEstado;
        this.nomeTag=nomeTag;
        if(cor!=null)
            this.cor = cor;
        else
            this.cor = "#5bc0de";

        this.url = url;
        this.aviso = aviso;
        if(apenasMeuGabinete!=null)
            this.apenasMeuGabinete = convertSimNaoToBoolean(apenasMeuGabinete);
        else
            this.apenasMeuGabinete = false;

        this.countLinha=countLinha;
    }

    public static boolean convertSimNaoToBoolean(String simNao) {
        Collator collator = Collator.getInstance(new Locale("pt", "BR"));
        collator.setStrength(Collator.PRIMARY);

        if (collator.equals("sim", simNao)) {
            return true;
        } else {
            return false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNrProcesso() {
        return nrProcesso;
    }

    public void setNrProcesso(String nrProcesso) {
        this.nrProcesso = nrProcesso;
    }

    public String getNomeTag() {
        return nomeTag;
    }

    public void setNomeTag(String nomeTag) {
        this.nomeTag = nomeTag;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public List<String> getListNomeTag() {
        return listNomeTag;
    }

    public List<String> getLisNomeTagSplit(){
        String[] tagNome =  getNomeTag().split("\\|");
        List<String> listNome = new ArrayList<>(Arrays.asList(tagNome));
        return listNome;
    }

    public void setListNomeTag(List<String> listNomeTag) {
        this.listNomeTag = listNomeTag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAviso() {
        return aviso;
    }

    public void setAviso(String aviso) {
        this.aviso = aviso;
    }

    public Boolean getApenasMeuGabinete() {
        return apenasMeuGabinete;
    }

    public void setApenasMeuGabinete(Boolean apenasMeuGabinete) {
        this.apenasMeuGabinete = apenasMeuGabinete;
    }

    public int getCountLinha() {
        return countLinha;
    }

    public void setCountLinha(int countLinha) {
        this.countLinha = countLinha;
    }


    public String getNrOab() {
        return nrOab;
    }

    public void setNrOab(String nrOab) {
        this.nrOab = nrOab;
    }

    public String getSiglaEstado() {
        return siglaEstado;
    }

    public void setSiglaEstado(String siglaEstado) {
        this.siglaEstado = siglaEstado;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(nrProcesso!=null){
            sb.append(escapeValue(nrProcesso)).append(",");
        }
        if(nrOab!=null){
            sb.append(escapeValue(nrOab)).append(",");
        }
        if(siglaEstado!=null){
            sb.append(escapeValue(siglaEstado)).append(",");
        }
        sb.append(escapeValue(nomeTag)).append(",");
        sb.append(escapeValue(cor)).append(",");
        sb.append(escapeValue(aviso)).append(",");
        sb.append(escapeValue(url)).append(",");
        sb.append(apenasMeuGabinete != null ? apenasMeuGabinete : "");
        return sb.toString();
    }

    private String escapeValue(String value) {
        if (value == null) {
            return "";
        }
        // Se o valor contiver vírgulas, coloque aspas duplas ao redor para evitar problemas no CSV
        if (value.contains(",")) {
            return "\"" + value + "\"";
        }
        return value;
    }
}
