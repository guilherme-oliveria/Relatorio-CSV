package br.jus.tjro.gabinete.service.local;

import br.jus.tjro.gabinete.builders.service.BuilderPessoaDocumentoService;
import br.jus.tjro.gabinete.fonte.dados.core.FonteDadosEnum;
import br.jus.tjro.gabinete.model.gab.pessoa.Pessoa;
import br.jus.tjro.gabinete.model.gab.pessoa.PessoaDocumento;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PessoaDocumentoServiceTest {
    @Test
    public void devePassarSemImportarNada() throws Exception {
        BuilderPessoaDocumentoService builder = new BuilderPessoaDocumentoService();
        PessoaDocumentoService service = builder.get();

        Pessoa pessoa = new Pessoa();
        pessoa.setIdPessoaLegado(7l);
        ProcessoParte processoParte = new ProcessoParte();
        processoParte.setPessoa(pessoa);

        List<PessoaDocumento> retorno = service.importaOuAtualizaDocumentosDaPessoa(processoParte, FonteDadosEnum.PJEPG);
        assertThat(retorno.size()).isEqualTo(0);
    }

    @Test
    public void deveImportarUmaParte() throws Exception {
        BuilderPessoaDocumentoService builder = new BuilderPessoaDocumentoService();
        PessoaDocumentoService service = builder.get();

        Pessoa pessoa = new Pessoa();
        pessoa.setIdPessoaLegado(6l);
        ProcessoParte processoParte = new ProcessoParte();
        processoParte.setPessoa(pessoa);

        List<PessoaDocumento> retorno = service.importaOuAtualizaDocumentosDaPessoa(processoParte, FonteDadosEnum.PJEPG);
        assertThat(retorno.size()).isEqualTo(1);
    }
}
