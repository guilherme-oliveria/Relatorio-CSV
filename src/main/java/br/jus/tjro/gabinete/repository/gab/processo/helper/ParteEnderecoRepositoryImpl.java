package br.jus.tjro.gabinete.repository.gab.processo.helper;

import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteEndereco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ParteEnderecoRepositoryImpl implements ParteEnderecoRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<ProcessoParteEndereco> pegarEnderecosComPaginacao(Pageable pageable, Long idParte) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ProcessoParteEndereco> criteria = builder.createQuery(ProcessoParteEndereco.class);
        Root<ProcessoParteEndereco> root = criteria.from(ProcessoParteEndereco.class);
//        Join<ProcessoParteEndereco, Endereco> owner = root.join("endereco");
        Predicate[] predicates = criarRestricoes(builder, root, idParte);
        criteria.where(predicates);

//        criteria.orderBy(builder.desc(root.getMap("endereco.logradouro")));
        TypedQuery<ProcessoParteEndereco> query = manager.createQuery(criteria);

        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, totalPagina(idParte));

    }

    private Predicate[] criarRestricoes(CriteriaBuilder builder, Root<ProcessoParteEndereco> root, Long idParte) {
        List<Predicate> predicates = new ArrayList<>();

//        predicates.add(builder.equal(root.join("processoParte").getMap("id"),idParte));

        predicates.add(builder.equal(root.get("processoParte"), idParte));

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<ProcessoParteEndereco> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long totalPagina(Long idParte) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

        Root<ProcessoParteEndereco> root = criteria.from(ProcessoParteEndereco.class);

        Predicate[] predicates = criarRestricoes(builder, root, idParte);

        criteria.where(predicates);
        criteria.select(builder.count(root));

        return manager.createQuery(criteria).getSingleResult();
    }

}
