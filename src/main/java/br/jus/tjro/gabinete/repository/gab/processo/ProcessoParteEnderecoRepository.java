package br.jus.tjro.gabinete.repository.gab.processo;

import br.jus.tjro.gabinete.model.gab.processo.ProcessoParte;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteEndereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProcessoParteEnderecoRepository extends JpaRepository<ProcessoParteEndereco, Long> {

    @Deprecated
    List<ProcessoParteEndereco> findAllByIdEnderecoLegado(Long idEnderecoLegado);

    List<ProcessoParteEndereco> findAllByIdEnderecoLegadoAndProcessoParte(Long idEnderecoLegado, ProcessoParte processoParte);
}
