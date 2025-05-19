package br.jus.tjro.gabinete.infrastructure.converter;

import br.jus.tjro.gabinete.model.gab.question.ControlTypeEnum;
import br.jus.tjro.gabinete.model.gab.question.QuestionBase;
import br.jus.tjro.gabinete.repository.QuestionBaseRepository;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestionConverterTests {

    private final QuestionBaseRepository repository = mock(QuestionBaseRepository.class);
    private final QuestionConverter converter = new QuestionConverter(repository);

    private final Set<QuestionBase> questionarios = new HashSet<>();
    private final String jsonNoBanco = "{\"chave\":\"valor\"}";
    private final String jsonMutiplosNoBanco = "{\"chave\":\"valor\",\"key\":\"valor\"}";

    public QuestionConverterTests() throws Exception {
        QuestionBase q1 = new QuestionBase(null,"chave", "labelQuestionario", false,
            ControlTypeEnum.TextBox, null, null, new HashMap<>(), null,null);
        QuestionBase q2 = new QuestionBase(null, "key", "questionario2", false,
            ControlTypeEnum.TextBox, null, null, new HashMap<>(), null,null);
        questionarios.add(q1);
        questionarios.add(q2);
        when(repository.findAll()).thenReturn(questionarios);
    }


    @Test
    public void recuperarDoBanco(){
        Set<QuestionBase> resultado = converter.convertToEntityAttribute(jsonNoBanco);
        assertEquals(2,resultado.size());
        assertEquals("valor",resultado.stream().filter(it -> it.getKey().equals("chave")).findAny().get().getValue());
    }

    @Test
    public void salvarNoBanco(){
        Set<QuestionBase> questionario = questionarios.stream().filter(it -> it.getKey().equals("chave")).collect(Collectors.toSet());
        questionario.forEach(it -> it.setValue("valor"));
        String resultado = converter.convertToDatabaseColumn(questionario);
        assertEquals(jsonNoBanco,resultado);
    }

    @Test
    public void salvarMutiplosNoBanco(){
        Set<QuestionBase> questoes = new HashSet<>(questionarios);
        questoes.forEach(it -> it.setValue("valor"));
        String resultado = converter.convertToDatabaseColumn(questoes);
        assertEquals(jsonMutiplosNoBanco,resultado);
    }
}
