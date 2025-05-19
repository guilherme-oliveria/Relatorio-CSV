package br.jus.tjro.gabinete.model.gab.question;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TypeEnum {

	EMAIL("email"),
    HIDDEN("hidden"),
    PASSWORD("password"),
    TEXT("text"),
    DATE("date");

	private final String label;

	TypeEnum(String label) {
		this.label = label;
	}

    @JsonValue
	public String getLabel() {
		return this.label;
	}
}
