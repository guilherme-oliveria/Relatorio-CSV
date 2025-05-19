package br.jus.tjro.gabinete.repository.gab.processo.helper;

import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteEndereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ParteEnderecoRepositoryQueries {
    Page<ProcessoParteEndereco> pegarEnderecosComPaginacao(Pageable pageable, Long idParte);
}
