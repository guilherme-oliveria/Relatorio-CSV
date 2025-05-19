package br.jus.tjro.gabinete.repository;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.question.ControlTypeEnum;
import br.jus.tjro.gabinete.model.gab.question.QuestionBase;
import br.jus.tjro.gabinete.model.gab.question.Rule;
import br.jus.tjro.gabinete.service.local.RevisorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class QuestionBaseRepository {

    private final RevisorService revisorService;
    private String tribunal;

    public QuestionBaseRepository(RevisorService revisorService, @Value("${tribunal:''}")
    String tribunal) {
        this.revisorService = revisorService;
        this.tribunal = tribunal;
    }

    public Set<QuestionBase> findAll() throws Exception {
        Set lista = new HashSet<>();
        lista.add(QuestionBase.checkBoxBuilder("redistribuir",
            "Para Redistribuição",
            null, 0, new Rule() {
                @Override
                public boolean activeBy(Processo processo, QuestionBase questionBase) {
                    return processo.getOrgaoJulgador().contains("PJESG-7");
                }
            }));
        lista.add(QuestionBase.checkBoxBuilder("perola",
            "Suspensão art. 40 Lei 6.830/80, Resp 1.340.553",
            "suspensão nos termos do art. 40 da Lei 6.830/80, considerando o Resp 1.340.553", 0, new Rule() {
                @Override
                public boolean activeBy(Processo processo, QuestionBase questionBase) {
                    return List.of("PJEPG-109","PJEPG-110").stream().anyMatch(processo.getOrgaoJulgador()::contains);
                }
            }));

        if(!tribunal.equals("tjmg")) {
            QuestionBase caixaurgente = new QuestionBase(null, "UrgenteAndamento",
                "Urgente", false, ControlTypeEnum.CheckBox, null,
                "", Set.of(), 1, new Rule() {
                @Override
                public boolean activeBy(Processo processo, QuestionBase questionBase) {
                    var nomeCaixa = processo.getCaixa().getNome().toLowerCase();
                    if(questionBase.getValue() == null &&
                        List.of("decisão liminar ou tutela",
                            "despacho alvará",
                            "julgamento extinção",
                            "urgente"
                        ).stream().anyMatch(nomeCaixa::contains))
                        questionBase.setValue("true");
                    return processo.getOrgaoJulgador().contains("PJEPG");
                }
            });
            lista.add(caixaurgente);
        }

        lista.add(QuestionBase.dropDownBuilder("tipoVoto","Tipo Voto",
            Map.of(
                "7","Conhecido e Provido",
                "9","Conhecido e Não Provido",
                "11","Conhecido e Provido em Parte",
                "13","Não Conhecido",
                "14","Não informado"),
            true,"",0, new Rule() {
                @Override
                public boolean activeBy(Processo processo, QuestionBase questionBase) {
                    return List.of("PJESG").stream().anyMatch(processo.getOrgaoJulgador()::contains);
                }
            }));

        if(tribunal.equals("tjmg")){
            lista.add(QuestionBase.dropDownBuilder("Lista_Medidas","Medidas Urgentes",
                Map.of(
                    "Alvará de Soltura","Alvará de Soltura",
                    "Mandado de Prisão","Mandado de Prisão",
                    "Outras medidas urgentes","Outras medidas urgentes",
                    "Designar Audiência Urgente","Designar Audiência Urgente"),
                false,"",0, new Rule() {
                    @Override
                    public boolean activeBy(Processo processo, QuestionBase questionBase) {
                        return List.of("[CRI]").stream().anyMatch(processo.getCaixa().getNome()::contains);
                    }
                }));

            lista.add(QuestionBase.dropDownBuilder("Lista_Medidas_APFD","Medidas Urgentes",
                Map.of(
                    "Alvará de Soltura","Alvará de Soltura",
                    "Mandado de Prisão","Mandado de Prisão",
                    "Arbitramento de Fiança","Arbitramento de Fiança"),
                false,"",0, new Rule() {
                    @Override
                    public boolean activeBy(Processo processo, QuestionBase questionBase) {
                        return List.of("[CPFD]").stream().anyMatch(processo.getCaixa().getNome()::contains);
                    }
                }));

            QuestionBase  documentoSigiloso = new QuestionBase(null,"sigilo",
                "Documento Sigiloso?",false, ControlTypeEnum.CheckBox,null,
                "",Set.of(), 1, new Rule() {
                @Override
                public boolean activeBy(Processo processo, QuestionBase questionBase) {
                    return List.of("[CIV]","[CRI]","[CPFD]","[CARTA]").stream().anyMatch(processo.getCaixa().getNome()::contains);
                }
            });
            QuestionBase caixaurgente = new QuestionBase(null, "sim_nao_liminar:check_urgente",
                "Urgente", false, ControlTypeEnum.CheckBox, null,
                "", Set.of(), 1, new Rule() {
                @Override
                public boolean activeBy(Processo processo, QuestionBase questionBase) {
                    return List.of("[CIV]").stream().anyMatch(processo.getCaixa().getNome()::contains);
                }
            });
            lista.add(caixaurgente);
            lista.add(documentoSigiloso);

            //[CARTA]

            lista.add(QuestionBase.dropDownBuilder("Lista_Decisoes_Carta","Despachar Processo",
                Map.of(
                    "Enviar para redistribuição","Enviar para redistribuição",
                    "Enviar para audiência","Enviar para audiência",
                    "Enviar para leilão","Enviar para leilão",
                    "Devolver Carta","Devolver carta",
                    "Preparar ato de comunicação","Preparar ato de comunicação"
                ),
                false,"",0, new Rule() {
                    @Override
                    public boolean activeBy(Processo processo, QuestionBase questionBase) {
                        return List.of("[CARTA]").stream().anyMatch(processo.getCaixa().getNome()::contains);
                    }
                }));
        }


        return lista;
    }
}
