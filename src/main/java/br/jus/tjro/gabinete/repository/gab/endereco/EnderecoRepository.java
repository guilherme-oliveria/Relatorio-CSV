package br.jus.tjro.gabinete.repository.gab.endereco;

import br.jus.tjro.gabinete.model.gab.endereco.Endereco;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EnderecoRepository extends CrudRepository<Endereco, Long> {


    @Query(
        value = "SELECT " +
            " pe.* " +
            "FROM " +
                "endereco pe " +
            "WHERE " +
                "id_endereco_legado in :idsEnderecoLegado " +
            "AND (" +
                "EXISTS(" +
                    "SELECT 1 " +
                    "FROM " +
                        "PROCESSO_PARTE_ENDERECO ppe " +
                        "JOIN PROCESSO_PARTE pp on(pp.ID = ppe.ID_PROCESSO_PARTE)" +
                        "JOIN processo p on(pp.id_processo = p.id) " +
                    "WHERE " +
                        "pe.id=ppe.id_endereco AND p.SISTEMA = :fonte )" +
            " OR NOT EXISTS" +
            "(SELECT " +
                "1 " +
            "FROM PROCESSO_PARTE_ENDERECO ppe WHERE pe.id=ppe.id_endereco))",nativeQuery = true)
    Endereco findByIdEnderecoLegadoAndFonteDados(@Param("idsEnderecoLegado") List<Long> ids,
                                                 @Param("fonte") String fonte);
}
