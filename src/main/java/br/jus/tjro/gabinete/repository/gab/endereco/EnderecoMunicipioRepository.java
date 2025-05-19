package br.jus.tjro.gabinete.repository.gab.endereco;

import br.jus.tjro.gabinete.model.gab.endereco.EnderecoMunicipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EnderecoMunicipioRepository extends JpaRepository<EnderecoMunicipio, Long> {
    EnderecoMunicipio findByDescricaoAndEstado_Uf(String descricao, String estado_uf);
}
