package br.jus.tjro.gabinete.infrastructure.json;

import br.jus.tjro.gabinete.model.gab.Papel;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PapelJsonTest {


    @Test
    public void descerializaPapel() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String, List<Papel>>> typeRef = new TypeReference<>() {
        };
        Map<String, List<Papel>> resultado = mapper.reader().forType(typeRef).readValue(json);
        assertEquals(2, resultado.size());
    }


    @Test
    public void serializarPapel() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Papel papel = new Papel("windson",List.of(new OrgaoJulgador("sistema-id","desc", Collections.emptyList(),"sigla", List.of())));
        mapper.writeValueAsString(papel);
    }

    public String json = """
        {
           "PJESG":[
              {
                 "papel":"Diretor de Secretaria",
                 "orgaoJulgador":[
                   \s
                 ],
                 "sistema":"PJESG"
              },
              {
                 "papel":"Administrador",
                 "orgaoJulgador":[
                   \s
                 ],
                 "sistema":"PJESG"
              },
              {
                 "papel":"Secretário da Sessão",
                 "orgaoJulgador":[
                   \s
                 ],
                 "sistema":"PJESG"
              },
              {
                 "papel":"Assessor",
                 "orgaoJulgador":[
                    {
                       "id":"PJESG-3",
                       "descricao":"Turma Recursal - Gabinete 03",
                       "sigla":"GAB-TR-03",
                       "idLegado":"3",
                       "sistema":"PJESG"
                    },
                    {
                       "id":"PJESG-4",
                       "descricao":"Turma Recursal - Gabinete 02",
                       "sigla":"GAB-TR-02",
                       "idLegado":"4",
                       "sistema":"PJESG"
                    },
                    {
                       "id":"PJESG-22",
                       "descricao":"Gabinete Des. Valdeci Castellar Citon",
                       "sigla":"GAB-SG-14",
                       "idLegado":"22",
                       "sistema":"PJESG"
                    },
                    {
                       "id":"PJESG-9",
                       "descricao":"Gabinete Des. Raduan Miguel",
                       "sigla":"GAB-SG-01",
                       "idLegado":"9",
                       "sistema":"PJESG"
                    },
                    {
                       "id":"PJESG-12",
                       "descricao":"Gabinete Des. José Torres Ferreira",
                       "sigla":"GAB-SG-04",
                       "idLegado":"12",
                       "sistema":"PJESG"
                    },
                    {
                       "id":"PJESG-15",
                       "descricao":"Gabinete Des. Isaias Fonseca Moraes",
                       "sigla":"GAB-SG-07",
                       "idLegado":"15",
                       "sistema":"PJESG"
                    },
                    {
                       "id":"PJESG-16",
                       "descricao":"Gabinete Des. Daniel Ribeiro Lagos",
                       "sigla":"GAB-SG-08",
                       "idLegado":"16",
                       "sistema":"PJESG"
                    }
                 ],
                 "sistema":"PJESG"
              }
           ],
           "PJEPG":[
              {
                 "papel":"Diretor de Secretaria",
                 "orgaoJulgador":[
                    {
                       "id":"PJEPG-113",
                       "descricao":"Porto Velho - Vara Infracional e de Execução de Medidas Socioeducativas",
                       "sigla":"PVH1JIJ",
                       "idLegado":"113",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-118",
                       "descricao":"Cacoal - 1ª Vara Cível",
                       "sigla":"CAC1CIV",
                       "idLegado":"118",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-119",
                       "descricao":"Cacoal - 2ª Vara Cível",
                       "sigla":"CAC2CIV",
                       "idLegado":"119",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-88",
                       "descricao":"Porto Velho - 2ª Vara Cível",
                       "sigla":"PVH2CIV",
                       "idLegado":"88",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-105",
                       "descricao":"Porto Velho - 4ª Vara de Família",
                       "sigla":"PVH4FAM",
                       "idLegado":"105",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-89",
                       "descricao":"Porto Velho - 3ª Vara Cível",
                       "sigla":"PVH3CIV",
                       "idLegado":"89",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-91",
                       "descricao":"Porto Velho - 5ª Vara Cível",
                       "sigla":"PVH5CIV",
                       "idLegado":"91",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-29",
                       "descricao":"Alta Floresta do Oeste - Vara Única",
                       "sigla":"AFOVUN",
                       "idLegado":"29",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-78",
                       "descricao":"Nova Brasilândia do Oeste - Vara Única",
                       "sigla":"NBOVUN",
                       "idLegado":"78",
                       "sistema":"PJEPG"
                    }
                 ],
                 "sistema":"PJEPG"
              },
              {
                 "papel":"Administrador",
                 "orgaoJulgador":[
                   \s
                 ],
                 "sistema":"PJEPG"
              },
              {
                 "papel":"Assessor",
                 "orgaoJulgador":[
                    {
                       "id":"PJEPG-129",
                       "descricao":"Ouro Preto do Oeste - 2ª Vara Cível",
                       "sigla":"OPO2CIV",
                       "idLegado":"129",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-2",
                       "descricao":"Porto Velho - 1º Juizado Especial da Fazenda Pública",
                       "sigla":"PVHJEFAZD1",
                       "idLegado":"2",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-3",
                       "descricao":"Porto Velho - 3º Juizado Especial Cível",
                       "sigla":"PVH3JEC",
                       "idLegado":"3",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-4",
                       "descricao":"Porto Velho - 1º Juizado Especial Cível",
                       "sigla":"PVH1JEC",
                       "idLegado":"4",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-5",
                       "descricao":"Porto Velho - 2º Juizado Especial Cível",
                       "sigla":"PVH2JEC",
                       "idLegado":"5",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-199",
                       "descricao":"Espigão do Oeste - CEJUSC",
                       "sigla":"EDOCEJUSC",
                       "idLegado":"199",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-136",
                       "descricao":"Vilhena - 1ª Vara Cível",
                       "sigla":"VIL1CIV",
                       "idLegado":"136",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-8",
                       "descricao":"Costa Marques - Vara Única",
                       "sigla":"COMVUN",
                       "idLegado":"8",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-105",
                       "descricao":"Porto Velho - 4ª Vara de Família",
                       "sigla":"PVH4FAM",
                       "idLegado":"105",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-109",
                       "descricao":"Porto Velho - 1ª Vara de Execuções Fiscais",
                       "sigla":"PVH1EFI",
                       "idLegado":"109",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-111",
                       "descricao":"Porto Velho - 1ª Vara de Fazenda Pública",
                       "sigla":"PVH1FAP",
                       "idLegado":"111",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-175",
                       "descricao":"Porto Velho - 2ª Vara Criminal",
                       "sigla":"PVH2CRI",
                       "idLegado":"175",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-80",
                       "descricao":"Buritis - 1ª Vara Genérica",
                       "sigla":"BUR1GEN",
                       "idLegado":"80",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-113",
                       "descricao":"Porto Velho - Vara Infracional e de Execução de Medidas Socioeducativas",
                       "sigla":"PVH1JIJ",
                       "idLegado":"113",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-49",
                       "descricao":"Espigão do Oeste - 1ª Vara Genérica",
                       "sigla":"EDO1GEN",
                       "idLegado":"49",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-114",
                       "descricao":"Porto Velho - Vara de Proteção à Infância e Juventude",
                       "sigla":"PVH2JIJ",
                       "idLegado":"114",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-50",
                       "descricao":"Espigão do Oeste - 2ª Vara Genérica",
                       "sigla":"EDO2GEN",
                       "idLegado":"50",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-115",
                       "descricao":"Jaru - 2ª Vara Cível",
                       "sigla":"JAR2CIV",
                       "idLegado":"115",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-55",
                       "descricao":"Ji-Paraná - 1º Juizado Especial",
                       "sigla":"JIPJESP",
                       "idLegado":"55",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-186",
                       "descricao":"Ouro Preto do Oeste - 1ª Vara Criminal",
                       "sigla":"OPO1CRIM",
                       "idLegado":"186",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-92",
                       "descricao":"Porto Velho - 6ª Vara Cível",
                       "sigla":"PVH6CIV",
                       "idLegado":"92",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-128",
                       "descricao":"Ouro Preto do Oeste - 1ª Vara Cível",
                       "sigla":"OPO1CIV",
                       "idLegado":"128",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-96",
                       "descricao":"Porto Velho - 8ª Vara Cível",
                       "sigla":"PVH8CIV",
                       "idLegado":"96",
                       "sistema":"PJEPG"
                    }
                 ],
                 "sistema":"PJEPG"
              },
              {
                 "papel":"Assessor TJRO",
                 "orgaoJulgador":[
                    {
                       "id":"PJEPG-117",
                       "descricao":"Pimenta Bueno - 2ª Vara Cível",
                       "sigla":"PIB2CIV",
                       "idLegado":"117",
                       "sistema":"PJEPG"
                    },
                    {
                       "id":"PJEPG-217",
                       "descricao":"Ariquemes - 1ª Vara Criminal",
                       "sigla":"ARI1CRI",
                       "idLegado":"217",
                       "sistema":"PJEPG"
                    }
                 ],
                 "sistema":"PJEPG"
              }
           ]
        }""";
}
