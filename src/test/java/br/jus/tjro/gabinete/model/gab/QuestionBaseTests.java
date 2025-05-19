package br.jus.tjro.gabinete.model.gab;

import br.jus.tjro.gabinete.model.gab.question.ControlTypeEnum;
import br.jus.tjro.gabinete.model.gab.question.QuestionBase;
import br.jus.tjro.gabinete.model.gab.question.TypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionBaseTests {

    @Test
    public void deveDarErroComChaveNull() {
        assertThrows(Exception.class, () ->
            new QuestionBase("valor",null,"label",false,ControlTypeEnum.TextBox,null,null,new HashMap<>(),null,null)
        );
    }

    @Test
    public void deveDarErroComOptionNull() {
        assertThrows(Exception.class, () ->
            new QuestionBase("valor",null,"label",false,ControlTypeEnum.DropDown,null,null, Set.of(),null,null)
        );
    }

    @Test
    public void deveDarErroComOptionVazio() {
        assertThrows(Exception.class, () ->
            new QuestionBase("valor",null,"label",false,ControlTypeEnum.DropDown,null,null,new HashMap<>(),null,null)
        );
    }

    @Test
    public void ciraComSucesso() throws Exception {
        QuestionBase q1 = new QuestionBase("valor", "chave", "label", false, ControlTypeEnum.TextBox, null, null, new HashMap<>(), null,null);
        QuestionBase q2 = QuestionBase.textBoxBuilder("chave", "label", false, null, null, null,null);
        QuestionBase q3 = QuestionBase.checkBoxBuilder("chave", "label",null,0,null);
        Map<String, String> options = Map.of("t1","r1","t2","r2");
        QuestionBase q4 = QuestionBase.dropDownBuilder("chave", "label",options,false,null,0,null);
        q4.setValue("t1");

        assertEquals(ControlTypeEnum.TextBox,q2.getControlType());
        assertEquals(ControlTypeEnum.CheckBox,q3.getControlType());
        assertEquals(ControlTypeEnum.DropDown,q4.getControlType());
        assertEquals(true,q4.activeBy(null));
    }

    @Test
    public void valoresPadroes() throws Exception {
        QuestionBase q1 = new QuestionBase("valor", "chave", "label", null,
            ControlTypeEnum.TextBox, null, null, Set.of(), null,null);
        assertEquals(TypeEnum.TEXT,q1.getType());
        assertEquals(1,q1.getOrder());
        assertFalse(q1.isRequired());
    }

    @Test
    public void seValorNaoEstaEmOptionsDeveDarErro() throws Exception {
        Map<String, String> options = Map.of("t1","r1","t2","r2");
        assertThrows(Exception.class, () -> {
            QuestionBase q4 = QuestionBase.dropDownBuilder("chave", "label", options, false, null, 0, null);
            q4.setValue("valorParaErro");
        });
    }

    @Test
    public void seDropDownPermiteValorNull() throws Exception {
        Map<String, String> options = Map.of("t1","r1","t2","r2");
        QuestionBase q4 = QuestionBase.dropDownBuilder("chave", "label",options,false,null,0,null);
    }


    @Test
    public void seCheckBoxValueDeveFazerParseParaBoolenOuSerNull() throws Exception {
        QuestionBase q1 = QuestionBase.checkBoxBuilder("chave", "label", null, 0,null);
        QuestionBase q2 = QuestionBase.checkBoxBuilder("chave", "label",null,0,null);
        q1.setValue("true");
        q1.setValue("false");
    }

    @Test
    public void seCheckBoxValueNaoBooleanoDeveSerFalse() throws Exception {
        QuestionBase q1 = QuestionBase.checkBoxBuilder("chave", "label", null, 0,null);
        q1.setValue("não Boleano");
        assertEquals("false",q1.getValue());
    }

    @Test
    public void testaResultadoParaSalvarNoBanco() throws Exception {
        QuestionBase q1 = new QuestionBase("valor", "chave", "label", false,
            ControlTypeEnum.TextBox, null, null, new HashMap<>(), null,null);
        q1.getKeyAndValue().entrySet().stream().forEach(it -> {
            assertEquals("chave",it.getKey());
            assertEquals("valor",it.getValue());
        });
        assertEquals(1,q1.getKeyAndValue().size());
    }


    @Test
    public void testandoRecuperarDoBanco() throws Exception {
        QuestionBase q1 = new QuestionBase("valor", "chave", "label", false,
            ControlTypeEnum.TextBox, null, null, new HashMap<>(), null,null);
        q1.getKeyAndValue().entrySet().stream().forEach(it -> {
            assertEquals("chave",it.getKey());
            assertEquals("valor",it.getValue());
        });
        assertEquals(1,q1.getKeyAndValue().size());
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(q1.getKeyAndValue());
        System.out.println(json);
    }
}
