package br.jus.tjro.gabinete.model.gab.enums;

public enum ExpedicaoExpedienteEnum {

	E("Sistema"),
    P("Diário Eletrônico"),
    D("Edital"),
	L("Carta Precatória"),
	C("Correios"),
	M("Central de Mandados"),
	T("Telefone"),
	S("Pessoalmente"),
	N("Comunicação");

	private final String label;

	ExpedicaoExpedienteEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}
	
	public boolean isExpedicaoFisica() {
		return (C.equals(this) || M.equals(this) || L.equals(this));
	}
	
	public boolean isExpedicaoRealizadaPessoalmente(){
		return (T.equals(this) || S.equals(this));
	}
	
	public boolean isExigeEndereco(){
		return (M.equals(this) || C.equals(this) || L.equals(this));
	}
	
	public boolean isExigeParteCadastradaComCertificado(){
		return E.equals(this);
	}
	
	public boolean isExigeTelefone(){
	    return T.equals(this);
	}
	
}
