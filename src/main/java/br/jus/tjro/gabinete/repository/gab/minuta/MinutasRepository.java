package br.jus.tjro.gabinete.repository.gab.minuta;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.minuta.Minuta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface MinutasRepository extends JpaRepository<Minuta, Long> {


    Optional<Minuta> findById(Long id);

    @Query("SELECT p from Minuta p where p.id in (:processos)")
    List<Minuta> findByIds(@Param("processos") List<Long> idProcessos);

    @Query("SELECT DISTINCT m from Minuta m " +
        " LEFT JOIN MinutaAnexo ma ON ma.minutaPai.id = m.id " +
        " LEFT JOIN Assinatura a ON a.id = m.assinatura.id " +
        " where m.processo.id = :id_processo " +
        " and (m.idMinutaSistemaLegado is null or m.idMinutaSistemaLegado = '') " +
        " and (ma.id is null OR (ma.idMinutaSistemaLegado is null or ma.idMinutaSistemaLegado = '') and a.assinatura is null)")
    Optional<Minuta> getMinutaEmElaboracaoByIdProcesso(@Param("id_processo") Long idProcesso);

    @Modifying
    @Query("update Minuta m set m.idMinutaSistemaLegado = null where m.id = :id")
    Integer removeIdLegado(@Param("id") Long idDocumento);


    @Query("SELECT p from Minuta p " +
        " left join fetch p.processo proc " +
        " left join proc.caixa caixa " +
        " where caixa.id = :idCaixa and p.idMinutaSistemaLegado is null and p.tiposDocumentos is not null" +
        " ORDER BY p.id DESC")
    List<Minuta> findTopMinutaTipoDocByCaixaIdOrderByDesc(Integer idCaixa, Pageable pageable);
}
