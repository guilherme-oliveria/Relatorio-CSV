package br.jus.tjro.gabinete.model.gab.question;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PersistenceEnum {

	Fluxo("fluxo");
    //DataBase("base"); // TODO no momento não sei como persistir esses formularios dinamicamente

	private final String label;

	PersistenceEnum(String label) {
		this.label = label;
	}

    @JsonValue
	public String getLabel() {
		return this.label;
	}
	
}
