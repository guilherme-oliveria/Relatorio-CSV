package br.jus.tjro.gabinete.model.gab.alvara;

import java.util.*;

public enum InstituicaoFinanceira {
    AGIBANK("121","Banco Agibank S.A."),
    BMG("318","Banco BMG S.A."),
    BRADESCO("237","Banco Bradesco S.A."),
    BS2("218","Banco BS2 S.A."),
    C6_BANK("336","Banco C6 S.A – C6 Bank"),
    CITIBANK("745","Banco Citibank S.A."),
    SICOOB_BANCOOB("756","Banco Cooperativo do Brasil S.A. – BANCOOB/SICOOB"),
    BANCO_SICREDI("748","Banco Cooperativo Sicredi S.A."),
    CREFISA("069","Banco Crefisa S.A."),
    BANCO_DA_AMAZONIA("003","Banco da Amazônia S.A."),
    DIGIO("335","Banco Digio S.A"),
    BANCO_DO_BRASIL("001","Banco do Brasil S.A."),
    BANPARA("037","Banco do Estado do Pará S.A."),
    BANRISUL("041","Banco do Estado do Rio Grande do Sul S.A."),
    BANCO_INTER("077","Banco Inter S.A."),
    BANCO_MERCANTIL("389","Banco Mercantil do Brasil S.A."),
    BNDES("007","Banco Nacional de Desenvolvimento Econômico e Social – BNDES"),
    ORIGINAL("212","Banco Original S.A."),
    SAFRA("422","Banco Safra S.A."),
    SANTANDER("033","Banco Santander (Brasil) S.A."),
    BRB("070","BRB – Banco de Brasília S.A."),
    CAIXA("104","Caixa Econômica Federal"),
    ITAÚ("341","Itaú Unibanco S.A."),
    KIRTON_BANK("399","Kirton Bank S.A. – Banco Múltiplo"),
    NUBANK("260","Nu Pagamentos S.A (Nubank)"),
    PAGSEGURO("290","Pagseguro Internet S.A"),
    PARANA_BANCO("254","Paraná Banco S.A."),
    STONE("197","Stone Pagamentos S.A"),
    BANCO_ADVANCED("117","Advanced Cc Ltda"),
    ALBATROSS("172","Albatross Ccv S.A"),
    ATIVA_INVESTIMENTOS("188","Ativa Investimentos S.A"),
    AVISTA("280","Avista S.A. Crédito, Financiamento e Investimento"),
    BT_CC("080","B&T Cc Ltda"),
    BANCO_RENNER("654","Banco A.J.Renner S.A."),
    BANCO_ABC("246","Banco ABC Brasil S.A."),
    BANCO_ABN_AMRO("075","Banco ABN AMRO S.A"),
    BANCO_ALFA("025","Banco Alfa S.A."),
    BANCO_ALVORADA("641","Banco Alvorada S.A."),
    BANCO_ANDBANK("065","Banco Andbank (Brasil) S.A."),
    BANCO_ARBI("213","Banco Arbi S.A."),
    BANCO_B3("096","Banco B3 S.A."),
    BANDEPE("024","Banco BANDEPE S.A."),
    BNP_PARIBAS("752","Banco BNP Paribas Brasil S.A."),
    BOCOM_BBM("107","Banco BOCOM BBM S.A."),
    BRADESCARD("063","Banco Bradescard S.A."),
    BRADESCO_BBI("036","Banco Bradesco BBI S.A."),
    BRADESCO_BERJ("122","Banco Bradesco BERJ S.A."),
    BRADESCO_CARTOES("204","Banco Bradesco Cartões S.A."),
    BRADESCO_FINANCIAMENTOS("394","Banco Bradesco Financiamentos S.A."),
    BTG_PACTUAL("208","Banco BTG Pactual S.A."),
    BANCO_CAIXA_GERAL("473","Banco Caixa Geral – Brasil S.A."),
    BANCO_CAPITAL("412","Banco Capital S.A."),
    BANCO_CARGILL("040","Banco Cargill S.A."),
    BANCO_CARREFOUR("368","Banco Carrefour"),
    BANCO_CEDULA("266","Banco Cédula S.A."),
    BANCO_CETELEM("739","Banco Cetelem S.A."),
    BANCO_CIFRA("233","Banco Cifra S.A."),
    BANCO_CLASSICO("241","Banco Clássico S.A."),
    BANCO_CREDIT_AGRICOLE("222","Banco Credit Agricole Brasil S.A."),
    CREDIT_SUISSE("505","Banco Credit Suisse (Brasil) S.A."),
    BANCO_CHINA("083","Banco da China Brasil S.A."),
    BANCO_DAYCOVAL("707","Banco Daycoval S.A."),
    BANDES("051","Banco de Desenvolvimento do Espírito Santo S.A."),
    BANCO_LA_NACION("300","Banco de La Nacion Argentina"),
    BANCO_LA_PROVINCIA("495","Banco de La Provincia de Buenos Aires"),
    BANCO_LA_REPUBLICA("494","Banco de La Republica Oriental del Uruguay"),
    BANESE("047","Banco do Estado de Sergipe S.A."),
    BANCO_NORDESTE("004","Banco do Nordeste do Brasil S.A."),
    BANCO_FAIR("196","Banco Fair Corretora de Câmbio S.A"),
    BANCO_FATOR("265","Banco Fator S.A."),
    BANCO_FIBRA("224","Banco Fibra S.A."),
    BANCO_FICSA("626","Banco Ficsa S.A."),
    BANCO_FINAXIS("094","Banco Finaxis S.A."),
    BANCO_GUANABARA("612","Banco Guanabara S.A."),
    BANCO_INBURSA("012","Banco Inbursa S.A."),
    BANCO_INDUSTRIAL("604","Banco Industrial do Brasil S.A."),
    BANCO_INDUSVAL("653","Banco Indusval S.A."),
    BANCO_INVESTCRED("249","Banco Investcred Unibanco S.A."),
    BANCO_ITAU_BBA("184","Banco Itaú BBA S.A."),
    BANCO_ITAU_CONSIGNADO("029","Banco Itaú Consignado S.A."),
    BANCO_ITAUBANK("479","Banco ItauBank S.A"),
    JP_MORGAN("376","Banco J. P. Morgan S.A."),
    BANCO_J_SAFRA("074","Banco J. Safra S.A."),
    BANCO_JOHN_DEERE("217","Banco John Deere S.A."),
    BANCO_KDB("076","Banco KDB S.A."),
    BANCO_KEB_HANA("757","Banco KEB HANA do Brasil S.A."),
    BANCO_LUSO_BRASILEIRO("600","Banco Luso Brasileiro S.A."),
    BANCO_MAXIMA("243","Banco Máxima S.A."),
    BANCO_MAXINVEST("720","Banco Maxinvest S.A."),
    BANCO_MIZUHO("370","Banco Mizuho do Brasil S.A."),
    BANCO_MODAL("746","Banco Modal S.A."),
    BANCO_MORGAN_STANLEY("066","Banco Morgan Stanley S.A."),
    BANCO_MUFG("456","Banco MUFG Brasil S.A."),
    BANCO_OLE_BONSUCESSO("169","Banco Olé Bonsucesso Consignado S.A."),
    BANCO_OLIVEIRA("111","Banco Oliveira Trust Dtvm S.A"),
    BANCO_ORIGINAL_AGRONEGOCIO("079","Banco Original do Agronegócio S.A."),
    BANCO_OURINVEST("712","Banco Ourinvest S.A."),
    BANCO_PAN("623","Banco PAN S.A."),
    BANCO_PAULISTA("611","Banco Paulista S.A."),
    BANCO_PINE("643","Banco Pine S.A."),
    BANCO_PORTO_REAL("658","Banco Porto Real de Investimentos S.A."),
    BANCO_RABOBANK("747","Banco Rabobank International Brasil S.A."),
    BANCO_RENDIMENTO("633","Banco Rendimento S.A."),
    BANCO_RIBEIRAO_PRETO("741","Banco Ribeirão Preto S.A."),
    RODOBENS("120","Banco Rodobens S.A."),
    BANCO_SEMEAR("743","Banco Semear S.A."),
    BANCO_SISTEMA("754","Banco Sistema S.A."),
    BANCO_SMARTBANK("630","Banco Smartbank S.A."),
    BANCO_SOCIETE_GENERALE("366","Banco Société Générale Brasil S.A."),
    BANCO_SOFISA("637","Banco Sofisa S.A."),
    BANCO_SUMITOMO("464","Banco Sumitomo Mitsui Brasileiro S.A."),
    BANCO_TOPAZIO("082","Banco Topázio S.A."),
    BANCO_TRIANGULO("634","Banco Triângulo S.A."),
    BANCO_TRICURY("018","Banco Tricury S.A."),
    BANCO_VOTORANTIM("655","Banco Votorantim S.A."),
    BANCO_VR("610","Banco VR S.A."),
    WESTERN_UNION("119","Banco Western Union do Brasil S.A."),
    WOORI_BANK("124","Banco Woori Bank do Brasil S.A."),
    BANCO_XP("348","Banco Xp S/A"),
    BANCOSEGURO("081","BancoSeguro S.A."),
    BANESTES("021","BANESTES S.A. Banco do Estado do Espírito Santo"),
    BANK_OF_AMERICA("755","Bank of America Merrill Lynch Banco Múltiplo S.A."),
    BARIGUI("268","Barigui Companhia Hipotecária"),
    BCV("250","BCV – Banco de Crédito e Varejo S.A."),
    BEXS_BANCO("144","BEXS Banco de Câmbio S.A."),
    BEXS_CORRETORA("253","Bexs Corretora de Câmbio S/A"),
    BGC_LIQUIDEZ("134","Bgc Liquidez Dtvm Ltda"),
    BNY_MELLON("017","BNY Mellon Banco S.A."),
    BPP("301","Bpp Instituição De Pagamentos S.A"),
    BR_PARTNERS("126","BR Partners Banco de Investimento S.A."),
    BRICKELL("092","Brickell S.A. Crédito, Financiamento e Investimento"),
    BRL("173","BRL Trust Distribuidora de Títulos e Valores Mobiliários S.A."),
    BROKER_BRASIL("142","Broker Brasil Cc Ltda"),
    BS2_DISTRIBUIDORA("292","BS2 Distribuidora de Títulos e Valores Mobiliários S.A."),
    CREDIT_SUISSE_HEDGING("011","C.Suisse Hedging-Griffo Cv S.A (Credit Suisse)"),
    CAROL("288","Carol Distribuidora de Títulos e Valor Mobiliários Ltda"),
    CARUANA("130","Caruana Scfi"),
    CASA_CREDITO("159","Casa Credito S.A"),
    CCM("016","Ccm Desp Trâns Sc E Rs"),
    CCR("089","Ccr Reg Mogiana"),
    CENTRAL_COOPERATIVA_ESPIRITO_SANTO("114","Central Cooperativa De Crédito No Estado Do Espírito Santo"),
    CHINA_CONSTRUCTION_BANK("320","China Construction Bank (Brasil) Banco Múltiplo S.A."),
    CITIBANK_NA("477","Citibank N.A."),
    CM_CAPITAL_MARKETS("180","Cm Capital Markets Cctvm Ltda"),
    CODEPE("127","Codepe Cvc S.A"),
    COMMERZBANK("163","Commerzbank Brasil S.A. – Banco Múltiplo"),
    CONFIDENCE("060","Confidence Cc S.A"),
    COOP_CENTRAL("085","Coop Central Ailos"),
    COOPERATIVA_CENTRAL_NORDESTE("097","Cooperativa Central de Crédito Noroeste Brasileiro Ltda."),
    COOPERATIVA_CREDITO_RURAL_OURO("286","Cooperativa de Crédito Rural De Ouro"),
    COOPERATIVA_CREDITO_PRIMAVERA("279","Cooperativa de Crédito Rural de Primavera Do Leste"),
    COOPERATIVA_CREDITO_RURAL_SAO_MIGUEL("273","Cooperativa de Crédito Rural de São Miguel do Oeste – Sulcredi/São Miguel"),
    CREDALIANCA("098","Credaliança Cooperativa de Crédito Rural"),
    CREDICOAMO("010","Credicoamo"),
    CRESOL("133","Cresol Confederação"),
    DACASA("182","Dacasa Financeira S/A"),
    DEUTSCHE_BANK("487","Deutsche Bank S.A. – Banco Alemão"),
    EASYNVEST("140","Easynvest – Título Cv S.A"),
    FACTA("149","Facta S.A. Cfi"),
    FRENTE("285","Frente Corretora de Câmbio Ltda."),
    GENIAL_INVESTIMENTOS("278","Genial Investimentos Corretora de Valores Mobiliários S.A."),
    GET_MONEY("138","Get Money Cc Ltda"),
    GOLDMAN_SACHS("064","Goldman Sachs do Brasil Banco Múltiplo S.A."),
    GUIDE("177","Guide Investimentos S.A. Corretora de Valores"),
    GUITTA("146","Guitta Corretora de Câmbio Ltda"),
    HAITONG("078","Haitong Banco de Investimento do Brasil S.A."),
    HIPERCARD("062","Hipercard Banco Múltiplo S.A."),
    HS_FINANCEIRA("189","HS Financeira S/A Crédito, Financiamento e Investimentos"),
    HSBC("269","HSBC Brasil S.A. – Banco de Investimento"),
    IB_CORRETORA("271","IB Corretora de Câmbio, Títulos e Valores Mobiliários S.A."),
    ICAP("157","Icap Do Brasil Ctvm Ltda"),
    ICBC("132","ICBC do Brasil Banco Múltiplo S.A."),
    ING("492","ING Bank N.V."),
    INTESA_SANPAOLO("139","Intesa Sanpaolo Brasil S.A. – Banco Múltiplo"),
    ITAÚ_UNIBANCO_HOLDING("652","Itaú Unibanco Holding S.A."),
    JP_MORGAN_CHASE("488","JPMorgan Chase Bank, National Association"),
    LASTRO_RDV("293","Lastro RDV Distribuidora de Títulos e Valores Mobiliários Ltda."),
    LECCA("105","Lecca Crédito, Financiamento e Investimento S/A"),
    LEVYCAM("145","Levycam Ccv Ltda"),
    MAGLIANO("113","Magliano S.A"),
    MERCADO_PAGO("323","Mercado Pago – Conta Do Mercado Livre"),
    MS_BANK("128","MS Bank S.A. Banco de Câmbio"),
    MULTIMONEY("137","Multimoney Cc Ltda"),
    NATIXIS("014","Natixis Brasil S.A. Banco Múltiplo"),
    NOVA_FUTURA("191","Nova Futura Corretora de Títulos e Valores Mobiliários Ltda."),
    NOVO_BANCO_CONTINENTAL("753","Novo Banco Continental S.A. – Banco Múltiplo"),
    OMNI("613","Omni Banco S.A."),
    PARATI("326","Parati – Crédito Financiamento e Investimento S.A."),
    PARMETAL("194","Parmetal Distribuidora de Títulos e Valores Mobiliários Ltda"),
    PERNAMBUCANAS("174","Pernambucanas Financ S.A"),
    PLANNER("100","Planner Corretora De Valores S.A"),
    PLURAL("125","Plural S.A. – Banco Múltiplo"),
    POLOCRED("093","Pólocred Scmepp Ltda"),
    PORTOCRED("108","Portocred S.A"),
    RB_CAPITAL("283","Rb Capital Investimentos Dtvm Ltda"),
    RENASCENCA("101","Renascenca Dtvm Ltda"),
    SAGITUR("270","Sagitur Corretora de Câmbio Ltda."),
    SCOTIABANK("751","Scotiabank Brasil S.A. Banco Múltiplo"),
    SENFF("276","Senff S.A. – Crédito, Financiamento e Investimento"),
    SENSO("545","Senso Ccvm S.A"),
    SERVICOOP("190","Servicoop"),
    SOCRED("183","Socred S.A"),
    SOROCRED("299","Sorocred Crédito, Financiamento e Investimento S.A."),
    STANDARD_CHARTERED_BANK("118","Standard Chartered Bank (Brasil) S/A–Bco Invest."),
    SUPER_PAGAMENTOS("340","Super Pagamentos e Administração de Meios Eletrônicos S.A."),
    TRAVELEX("095","Travelex Banco de Câmbio S.A."),
    TREVISO("143","Treviso Corretora de Câmbio S.A."),
    TULLETT_PREBON("131","Tullett Prebon Brasil Cvc Ltda"),
    UBS_BRASIL("129","UBS Brasil Banco de Investimento S.A."),
    UNICRED_RS("091","Unicred Central do Rio Grande do Sul"),
    UNICRED("136","Unicred Cooperativa"),
    UNIPRIME_CENTRAL("099","UNIPRIME Central – Central Interestadual de Cooperativas de Crédito Ltda."),
    UNIPRIME_NORTE_PARANA("084","Uniprime Norte do Paraná – Coop de Economia eCrédito Mútuo dos Médicos, Profissionais das Ciências"),
    VIPS("298","Vips Cc Ltda"),
    VORTX("310","Vortx Distribuidora de Títulos e Valores Mobiliários Ltda"),
    XP("102","Xp Investimentos S.A");

    private static final List<InstituicaoFinanceira> list;

    private static final Map<String, String> BY_CODIGO = new HashMap<>();

    private static final LinkedHashMap<String, String> LISTA_COM_CODIGO = new LinkedHashMap<String, String>();

    static {
        list = Arrays.asList(InstituicaoFinanceira.values());
    }

    static {
        for (InstituicaoFinanceira e : values()) {
            BY_CODIGO.put(e.codigo, e.descricao);
        }
    }

    static {
        for (InstituicaoFinanceira e : values()) {
            LISTA_COM_CODIGO.put(e.codigo, e.descricao);
        }
    }

    private String codigo;
    private String descricao;

    InstituicaoFinanceira(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static List<InstituicaoFinanceira> getList() {
        return list;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static String descricaoOfCodigo(String codigo) {
        return BY_CODIGO.get(codigo);
    }

    public static  LinkedHashMap<String, String> getListaComCodigo(){
        for (InstituicaoFinanceira e : values()) {
            LISTA_COM_CODIGO.put(e.codigo, e.descricao);
        }
        return LISTA_COM_CODIGO;
    }
}


