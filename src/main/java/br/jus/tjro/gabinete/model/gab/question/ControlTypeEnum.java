package br.jus.tjro.gabinete.model.gab.question;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ControlTypeEnum {

	TextBox("textbox"),
    DropDown("dropdown"),
    CheckBox("checkbox");

	private final String label;

	ControlTypeEnum(String label) {
		this.label = label;
	}

    @JsonValue
	public String getLabel() {
		return this.label;
	}
	
}
