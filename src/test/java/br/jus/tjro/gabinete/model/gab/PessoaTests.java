package br.jus.tjro.gabinete.model.gab;

import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.pessoa.PessoaDocumento;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PessoaTests {

    @Test
    public void documentoNull(){
        Pessoa pessoa = new Pessoa();
        assertEquals("DESCONHECIDO",pessoa.getDocumentoPorTipo("stz"));
    }

    @Test
    public void documentoComListaVazia(){
        Pessoa pessoa = new Pessoa();
        pessoa.setPessoaDocumentos(new ArrayList<>());
        assertEquals("DESCONHECIDO",pessoa.getDocumentoPorTipo("stz"));
    }

    @Test
    public void comParametroNullEListaVazia(){
        Pessoa pessoa = new Pessoa();
        pessoa.setPessoaDocumentos(new ArrayList<>());
        assertEquals("DESCONHECIDO",pessoa.getDocumentoPorTipo(null));
    }

    @Test
    public void deveVoltarDesconhecido(){
        Pessoa pessoa = new Pessoa();
        PessoaDocumento pessoaDocumento = new PessoaDocumento();
        pessoaDocumento.setDocumento(null);
        pessoaDocumento.setTipoDocumento("stz");
        pessoa.setPessoaDocumentos(Arrays.asList(pessoaDocumento));
        assertEquals("DESCONHECIDO",pessoa.getDocumentoPorTipo("stz"));
    }
}
