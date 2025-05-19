package br.jus.tjro.gabinete.repository.gab.endereco;

import br.jus.tjro.gabinete.model.gab.endereco.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Estado findByUf(String s);
}
