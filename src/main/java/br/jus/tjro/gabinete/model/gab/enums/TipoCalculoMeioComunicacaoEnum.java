package br.jus.tjro.gabinete.model.gab.enums;

public enum TipoCalculoMeioComunicacaoEnum {

	CD("Do cumprimento da diligência"),
	JCD("Da juntada da certidão da diligência");
	
	private final String label;
	
	TipoCalculoMeioComunicacaoEnum(String label){
		this.label = label;
	}

    public String getLabel() {
        return label;
    }
}
