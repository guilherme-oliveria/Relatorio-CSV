package br.jus.tjro.gabinete.model.gab.question;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class QuestionBase {

    private final Rule rule;
    private final PersistenceEnum persistence;
    private String value;
    private final String key;
    private final String label;
    private final boolean required;
    private Integer order;
    private final ControlTypeEnum controlType;
    private final TypeEnum type;
    private final String toolTip;
    private final Set<Option> options;


    public QuestionBase(String value, String key,
                        String label, Boolean required,
                        ControlTypeEnum controlType,
                        TypeEnum type, String toolTip,
                        Map<String, String> options, Integer order,Rule rule) throws Exception {
        this(value,key,label,required,controlType,type,toolTip,
            options == null ? null : options.entrySet().stream().map(it -> new Option(it.getKey(),it.getValue())).collect(Collectors.toSet())
            ,order, rule);
        Set<Option> optionsTemp = null;
    }

    @JsonCreator
    public QuestionBase(@JsonProperty("value") String value, @JsonProperty("key") String key,
                        @JsonProperty("label") String label, @JsonProperty("required") Boolean required,
                        @JsonProperty("controlType") ControlTypeEnum controlType,
                        @JsonProperty("type") TypeEnum type, @JsonProperty("toolTip") String toolTip,
                        @JsonProperty("options") Set<Option> options, @JsonProperty("order") Integer order,
                        @JsonProperty(value = "rule")  Rule rule) throws Exception {
        if(key==null)
            throw new Exception("A key não pode ser null");
        if(ControlTypeEnum.DropDown == controlType && (options == null || options.size() < 1))
            throw new Exception("Options não pode ser null quando criar um DropDown");

        this.value = value;
        this.key = key;
        this.label = label;
        this.required = required != null && required;
        this.controlType = controlType;
        this.type = type == null ? TypeEnum.TEXT : type;
        this.toolTip = toolTip;
        this.options = options;
        this.order = order;
        this.rule = rule;
        this.persistence = PersistenceEnum.Fluxo;

        validaValue(value);
    }

    public static QuestionBase textBoxBuilder(String key,String label,boolean required,TypeEnum type,String toolTip,Integer order,Rule rule) throws Exception {
        return new QuestionBase(null,key,label,required,ControlTypeEnum.TextBox,type,toolTip,Set.of(),order,null);
    }

    public static QuestionBase checkBoxBuilder(String key,String label,String toolTip,Integer order,Rule rule) throws Exception {
        return new QuestionBase("false",key,label,false,ControlTypeEnum.CheckBox,null,toolTip,Set.of(),order,rule);
    }

    public static QuestionBase dropDownBuilder(String key,String label, Map<String, String> options,
                                               boolean required,String toolTip,Integer order,Rule rule) throws Exception {
        return new QuestionBase(null,key,label,required,ControlTypeEnum.DropDown,null,toolTip,options,order,rule);
    }

    public String getValue() {
        return value;
    }

    public String getValueOption() {
        String valueOption =value;
        if(ControlTypeEnum.DropDown == controlType && value != null){
            valueOption = getOptions().stream()
                .filter(it -> it.getKey().equals(value))
                .map(it -> it.getValue())
                .findFirst()
                .orElse(null);        
       }

        return valueOption;
    }

    public void setValue(String value) {
        try {
            if(validaValue(value))
                this.value = value;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atribuir valor",e);
        }
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public boolean isRequired() {
        return required;
    }

    public int getOrder() {
        return order == null ? 1 : order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ControlTypeEnum getControlType() {
        return controlType;
    }

    public TypeEnum getType() {
        return type;
    }

    public String getToolTip() {
        return toolTip;
    }

    public Set<Option> getOptions() {
        return options;
    }

    public PersistenceEnum getPersistence() {
        return persistence;
    }

    @JsonIgnore
    public Map<String,String> getKeyAndValue(){
        if(value == null)
            return null;
        Map<String,String> retorno = new HashMap();
        retorno.put(key, value);
        return retorno;
    }

    @JsonIgnore
    public boolean activeBy(Processo proceso){
        if(rule==null)
            return true;
        return rule.activeBy(proceso,this);
    }

    @JsonIgnore
    private boolean validaValue(String value) throws Exception {
         if(ControlTypeEnum.DropDown == controlType && value != null && !options.contains(new Option(value,"naoImporta")))
            throw new Exception("O value não esta presente em options");
         else if(ControlTypeEnum.CheckBox == controlType && value != null){
             if(!(value.equals("true") || value.equals("false"))){
                 this.value = "false";
                 return false;
             }
         }
         return true;
    }
}
