package br.jus.tjro.gabinete.repository.gab.processo.helper;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.gab.processo.ProcessoParteExpediente;
import br.jus.tjro.gabinete.repository.gab.filter.ProcessoParteExpedienteFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessoParteExpedienteRepositoryImpl implements ProcessoParteExpedienteRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<ProcessoParteExpediente> filtraProcessoParteExpediente(Processo processo, ProcessoParteExpedienteFilter processoParteExpedienteFilter, Pageable pageable, String search) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ProcessoParteExpediente> criteria = builder.createQuery(ProcessoParteExpediente.class);
        Root<ProcessoParteExpediente> root = criteria.from(ProcessoParteExpediente.class);
        Predicate[] predicates = criarRestricoes(processoParteExpedienteFilter, builder, root, search, processo);
        criteria.where(predicates);
        Join joinProcessoExpediente = root.join("processoExpediente");
        criteria.orderBy(builder.desc(joinProcessoExpediente.get("dtCriacao")), builder.desc(root.get("idLegado")));
        TypedQuery<ProcessoParteExpediente> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, totalPagina(processoParteExpedienteFilter, search, processo));
    }

    private Predicate[] criarRestricoes(ProcessoParteExpedienteFilter processoParteExpedienteFilter, CriteriaBuilder builder, Root<ProcessoParteExpediente> root, String search, Processo processo) {
        List<Predicate> predicates = new ArrayList<>();
        Join joinProcessoExpediente = root.join("processoExpediente");
        Join joinProcesso = joinProcessoExpediente.join("processo");

        predicates.add(builder.equal(joinProcesso.get("id"), processo.getId()));
        Predicate   predicateIdLegado = null,
                    predicateFechado= null,
                    predicatePessCiencia= null,
                    predicatePessParte= null,
                    predicateProcuradoria= null;

        if(search != null && !search.equals("")) {

            // idLegado
            try {
                Long idLegado = Long.parseLong(search);
                predicateIdLegado = builder.equal(builder.lower(root.get("idLegado")), idLegado);
            } catch (Exception e) {
            }

            // Fechado ou Aberto
            if(search.equals("fechado")) {
                predicateFechado = builder.isTrue(root.get("fechado"));
            }else if(search.equals("aberto")) {
                predicateFechado = builder.isFalse(root.get("fechado"));
            }

            if(predicateIdLegado != null || predicateFechado != null) {
                if(predicateIdLegado != null) predicates.add(predicateIdLegado);
                if(predicateFechado != null) predicates.add(predicateFechado);
                return predicates.toArray(new Predicate[predicates.size()]);
            }

            // Pessoas
            String nomeParte = "%" + search.toUpperCase() + "%";

            Join joinPessoaCiente = root.join("pessoaCiencia", JoinType.LEFT);
            Join joinPessoaParte = root.join("pessoaParte", JoinType.INNER);
            Join joinProcuradoria = root.join("procuradoria", JoinType.LEFT);

            predicatePessCiencia = builder.like(builder.upper(joinPessoaCiente.get("nome")), nomeParte);
            predicatePessParte = builder.like(builder.upper(joinPessoaParte.get("nome")), nomeParte);
            predicateProcuradoria = builder.like(builder.upper(joinProcuradoria.get("descricao")), nomeParte);

            predicates.add(builder.or(
                predicatePessCiencia,
                predicatePessParte,
                predicateProcuradoria));
        }

        if (processoParteExpedienteFilter.getIdLegado() != null) {
            predicates.add(builder.equal(builder.lower(root.get("idLegado")), processoParteExpedienteFilter.getIdLegado().toString()));
        }

        if (processoParteExpedienteFilter.getFechado() != null) {
            predicates.add(builder.equal(root.get("fechado"), processoParteExpedienteFilter.getFechado()));
        }

        if (processoParteExpedienteFilter.getDataCriacao() != null) {
            predicates.add(builder.equal(joinProcessoExpediente.get("dtCriacao"), processoParteExpedienteFilter.getDataCriacao()));
        }

        if (processoParteExpedienteFilter.getDataCiencia() != null) {
            predicates.add(builder.equal(root.get("dtCienciaParte "), processoParteExpedienteFilter.getDataCiencia()));
        }

        if (processoParteExpedienteFilter.getPessoaCiencia() != null) {
            Join joinPessoaCiente = root.join("pessoaCiencia");
            String nomeParte = "%" + processoParteExpedienteFilter.getPessoaCiencia().toUpperCase() + "%";
            Expression<String> expression = joinPessoaCiente.get("nome");
            predicates.add(builder.like(expression, nomeParte));
        }

        if (processoParteExpedienteFilter.getPessoaParte() != null) {
            Join joinPessoaParte = root.join("pessoaParte");
            String nomeParte = "%" + processoParteExpedienteFilter.getPessoaParte().toUpperCase() + "%";
            Expression<String> expression = joinPessoaParte.get("nome");
            predicates.add(builder.like(expression, nomeParte));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<ProcessoParteExpediente> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long totalPagina(ProcessoParteExpedienteFilter processoParteExpedienteFilter, String search, Processo processo) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

        Root<ProcessoParteExpediente> root = criteria.from(ProcessoParteExpediente.class);

        Predicate[] predicates = criarRestricoes(processoParteExpedienteFilter, builder, root, search, processo);

        criteria.where(predicates);
        criteria.select(builder.count(root));

        return manager.createQuery(criteria.distinct(true)).getSingleResult();

    }

}
