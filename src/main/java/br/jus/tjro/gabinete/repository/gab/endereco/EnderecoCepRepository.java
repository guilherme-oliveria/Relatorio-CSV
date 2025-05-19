package br.jus.tjro.gabinete.repository.gab.endereco;

import br.jus.tjro.gabinete.model.gab.endereco.EnderecoCep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EnderecoCepRepository extends JpaRepository<EnderecoCep, Long> {
    EnderecoCep findByNumero(String s);
}
