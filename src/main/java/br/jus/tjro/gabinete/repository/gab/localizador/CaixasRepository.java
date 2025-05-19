package br.jus.tjro.gabinete.repository.gab.localizador;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.localizador.Caixa;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface CaixasRepository extends CrudRepository<Caixa, Integer> {

    @Query("SELECT new Caixa(c.id, c.nome, count(*)) "
        + "FROM Processo p "
        + "JOIN p.caixa c "
        + "where p.tarefaEnum in(:tarefasVisiveis) "
        + "and p.orgaoJulgadorObj in(:idOJs) "
        + "group by c.id, c.nome "
        + "order by c.nome")
    List<Caixa> obtemListaCaixasAgrupadas(@Param("tarefasVisiveis") List<TarefaEnum> t, @Param("idOJs") List<OrgaoJulgador> idOJs);

    @Query("SELECT new Caixa(c.id, c.nome, count(*), p.tarefaEnum) "
        + "FROM Processo p "
        + "JOIN p.caixa c "
        + "where p.tarefaEnum in(:tarefasVisiveis) "
        + "and p.orgaoJulgadorObj = :idOJ "
        + "group by c.id, c.nome, p.tarefaEnum "
        + "order by c.nome")
    List<Caixa> obtemListaCaixas(@Param("tarefasVisiveis") List<TarefaEnum> t, @Param("idOJ") OrgaoJulgador idOJ);

    @Query("SELECT new Caixa(c.id, c.nome) "
        + "FROM Caixa c "
        + "Order by c.nome asc")
    List<Caixa> findByTodos();

    Optional<Caixa> findByNome(String nome);
}
