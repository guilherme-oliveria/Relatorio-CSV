package br.jus.tjro.gabinete.repository.gab.processo.helper;

import br.jus.tjro.gabinete.Tarefas.core.TarefaEnum;
import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebida;
import br.jus.tjro.gabinete.model.gab.processo.Processo;
import br.jus.tjro.gabinete.model.webjud.OrgaoJulgador;
import br.jus.tjro.gabinete.repository.gab.filter.ProcessoFilter;
import jakarta.persistence.NoResultException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.jus.tjro.gabinete.model.gab.minuta.MinutaRecebidaStatus.PENDENTE;

public class ProcessosRepositoryImpl implements ProcessosRepositoryQueries {

    private static final int PARAMETER_LIMIT = 800;
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Processo> filtrarProcessosComPaginacaoEOrgaoJulgador(ProcessoFilter processoFilter, Pageable pageable, int caixa,
                                                                     List<TarefaEnum> tarefas, List<String> idOJ) {

        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Processo> criteria = builder.createQuery(Processo.class);
        Root<Processo> root = criteria.from(Processo.class);
        Predicate[] predicates = criarRestricoes(processoFilter, builder, root, caixa, tarefas, idOJ);
        criteria.where(predicates);
        criteria = this.selectOrderBy(processoFilter, criteria, builder, root);
        TypedQuery<Processo> query = manager.createQuery(criteria.distinct(true));

        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, totalPagina(processoFilter, caixa, tarefas, idOJ));

    }

    @Override
    public Page<Processo> filtrarProcessosComPaginacaoEOrgaoJulgadorDoLocalizadorV3(ProcessoFilter processoFilter,
                                                                                    Pageable pageable,
                                                                                    List<Long> idProcessos,
                                                                                    List<TarefaEnum> tarefas,
                                                                                    List<String> idOJ) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Processo> criteria = builder.createQuery(Processo.class);
        Root<Processo> root = criteria.from(Processo.class);
        Predicate[] predicates = criarRestricoes(processoFilter, builder, root, 0, tarefas, idOJ,idProcessos, new ArrayList<>());
        criteria.where(predicates);
        criteria = this.selectOrderBy(processoFilter, criteria, builder, root);
        TypedQuery<Processo> query = manager.createQuery(criteria.distinct(true));

        adicionarRestricoesDePaginacao(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, totalPagina(processoFilter, 0, tarefas, idOJ,idProcessos, new ArrayList<>()));
    }

    private CriteriaQuery<Processo> selectOrderBy(ProcessoFilter processoFilter, CriteriaQuery<Processo> criteria, CriteriaBuilder builder, Root<Processo> root) {
        try {
            if (processoFilter.getSentido() != null && !processoFilter.getSentido().equals("") && processoFilter.getOrdenacao() > 0) {
                String orderField = "prioridade";
                switch (processoFilter.getOrdenacao()) {
                    case 1:
                        orderField = "tempoConcluso";
                        break;
                    case 2:
                        orderField = "dataUltimaDistribuicao";
                        break;
                    case 3:
                        orderField = "dataEntrada";
                        break;
                    case 4:
                        orderField = "ultimoMovimento";
                        break;
                }
                if (processoFilter.getSentido().equals("desc"))
                    criteria.orderBy(builder.desc(root.get(orderField)));
                else
                    criteria.orderBy(builder.asc(root.get(orderField)));
            } else {
                criteria.orderBy(builder.desc(root.get("possuiLiminar")),builder.desc(root.get("prioridade")), builder.desc(root.get("totalPrioridade")), builder.asc(root.get("dataEntrada")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return criteria;
        }
    }

    @Override
    public Processo buscaOsProcessosQueDevemSincronizar(Date oldDate) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Processo> criteria = builder.createQuery(Processo.class);
        Root<Processo> root = criteria.from(Processo.class);

        List<Predicate> predicates = new ArrayList<>();
        Predicate exp1 = builder.lessThanOrEqualTo(root.get("dataSincronizacao"),builder.literal(oldDate));
        Predicate exp2 = builder.isNull(root.<Date>get("dataSincronizacao"));
        predicates.add(builder.or(exp1, exp2));
        predicates.add(builder.notEqual(root.<TarefaEnum>get("tarefaEnum"),TarefaEnum.NaoConcluso));
        try {
            criteria.where(predicates.toArray(new Predicate[predicates.size()]));
            criteria.orderBy(builder.asc(root.get("dataSincronizacao")));
            TypedQuery<Processo> query = manager.createQuery(criteria);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Page<Processo> findByNumeroProcesso(String numeroProcesso, Usuario usuario, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Processo> criteria = builder.createQuery(Processo.class);
        Root<Processo> root = criteria.from(Processo.class);

        Predicate[] predicates = {
            builder.like(builder.lower(root.get("numeroProcesso")),"%" + numeroProcesso.toLowerCase() + "%"), root.get("orgaoJulgadorObj").in(usuario.getOrgaosJulgadoresCompleto())
        };

        criteria.where(predicates);
        TypedQuery<Processo> query = manager.createQuery(criteria);
        adicionarRestricoesDePaginacao(query,pageable);
         return  new PageImpl<>(query.getResultList(), pageable, totalPaginaNumeroProcesso(numeroProcesso,usuario.getOrgaosJulgadoresCompleto()));
    }

    @Override
    public Page<Processo> findComMinutasRecebidas(ProcessoFilter processoFilter, List<String> idOJ, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Processo> criteria = builder.createQuery(Processo.class);
        Root<Processo> root = criteria.from(Processo.class);

        Subquery<MinutaRecebida> subquery = criteria.subquery(MinutaRecebida.class);
        Root<MinutaRecebida> subRootEntity = subquery.from(MinutaRecebida.class);
        Subquery<MinutaRecebida> subQuery = subquery.select(subRootEntity).where(
            builder.equal(subRootEntity.get("status"),PENDENTE),
            builder.equal(subRootEntity.get("processo"),root.get("id"))
            );

        List<Predicate> predicateList = Stream.of(criarRestricoes(processoFilter, builder, root, 0, null, idOJ, null, new ArrayList<>()))
            .collect(Collectors.toList());
        predicateList.add(builder.exists(subQuery));
        Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);

        criteria.where(predicateList.toArray(new Predicate[predicateList.size()]));
        // TODO ainda sera definido a ordem de exibição, jonatas recomendou dtcriação (provavelmente sera isso)
        // criteria.orderBy(builder.desc(root.get("prioridade")), builder.asc(root.get("dataEntrada")));
        TypedQuery<Processo> query = manager.createQuery(criteria.distinct(true));
        adicionarRestricoesDePaginacao(query, pageable);
        Long totalPagina = totalPagina(predicates);
        return new PageImpl<>(query.getResultList(), pageable, totalPagina);
    }

    @Override
    public Page<Processo> getProcessosPorOJETarefaRevisarPaginada(ProcessoFilter processoFilter, Pageable pageable, List<OrgaoJulgador> revisadosPor, TarefaEnum revisar) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Processo> criteria = builder.createQuery(Processo.class);
        Root<Processo> root = criteria.from(Processo.class);
        Predicate[] predicates = criarRestricoes(processoFilter, builder, root, List.of(revisar), revisadosPor);
        criteria.where(predicates);
        criteria.orderBy(builder.desc(root.get("prioridade")), builder.asc(root.get("dataEntrada")));
        TypedQuery<Processo> query = manager.createQuery(criteria.distinct(true));

        adicionarRestricoesDePaginacao(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, totalPagina(processoFilter, 0, List.of(revisar), new ArrayList<>(), new ArrayList<>(), revisadosPor));
    }

    private Predicate[] criarRestricoes(ProcessoFilter processoFilter, CriteriaBuilder builder, Root<Processo> root, List<TarefaEnum> tarefas, List<OrgaoJulgador> revisadosPor) {
        return criarRestricoes(processoFilter,builder,root,0,tarefas,new ArrayList<>(),new ArrayList<>(),revisadosPor);
    }

    private Predicate[] criarRestricoes(ProcessoFilter processoFilter, CriteriaBuilder builder, Root<Processo> root, int caixa, List<TarefaEnum> tarefa, List<String> idOJ) {
        return criarRestricoes(processoFilter,builder,root,caixa,tarefa,idOJ,new ArrayList<>(),new ArrayList<>());
    }

    private Predicate[] criarRestricoes(ProcessoFilter processoFilter, CriteriaBuilder builder, Root<Processo> root,
                                        int caixa, List<TarefaEnum> tarefas, List<String> idOJ, List<Long> idsProcesso, List<OrgaoJulgador> orgaoJulgadores) {
        List<Predicate> predicates = new ArrayList<>();

        if (caixa > 0)
            predicates.add(builder.equal(root.get("caixa"), caixa));

        if(idsProcesso != null && idsProcesso.size() > 0)
            predicates.add(getPredicatesInOracleLimit(builder,root,idsProcesso));

        if(tarefas != null)
            predicates.add(root.get("tarefaEnum").in(tarefas));

        if(idOJ !=null && !idOJ.isEmpty())
            predicates.add(root.get("orgaoJulgadorObj").in(idOJ.stream().map(OrgaoJulgador::new).collect(Collectors.toList())));
        else if(!orgaoJulgadores.isEmpty())
            predicates.add(root.get("orgaoJulgadorObj").in(orgaoJulgadores));

        if (!StringUtils.isEmpty(processoFilter.getNumeroProcesso())) {
            predicates.add(builder.like(builder.lower(root.get("numeroProcesso")),
                "%" + processoFilter.getNumeroProcesso().toLowerCase() + "%"));
        }

        if (processoFilter.getDataConclusoDe() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("dataEntrada"), processoFilter.getDataConclusoDe()));
        }

        if (processoFilter.getDataConclusoAte() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("dataEntrada"), processoFilter.getDataConclusoAte()));
        }

        if (processoFilter.getAssuntos() != null && processoFilter.getAssuntos().size() > 0) {
            Join join = root.join("processoAssuntos");
            List<Long> codigos = processoFilter.getAssuntos();
            Expression<Long> expression = join.get("idAssunto");
            predicates.add(expression.in(codigos));
        }

        if (processoFilter.getMovimentos() != null && processoFilter.getMovimentos().size() > 0) {
            Join join = root.join("processoMovimentos");
            List<Long> codigos = processoFilter.getMovimentos();
            Expression<Long> expression = join.get("movimento");
            predicates.add(expression.in(codigos));
        }

        if (processoFilter.getClasses() != null && processoFilter.getClasses().size() > 0) {
            Expression<Long> expression = root.get("tpuClasse");
            predicates.add(expression.in(processoFilter.getClasses()));
        }

        if (processoFilter.getTags() != null && processoFilter.getTags().size() > 0) {
            //tags
            Join joinProcessoTag = root.join("tags");
            Join joinTag = joinProcessoTag.join("tag");
            List<Long> codigos = processoFilter.getTags();
            Expression<Long> expression = joinTag.get("id");
            predicates.add(expression.in(codigos));
        }

        if (!StringUtils.isEmpty(processoFilter.getCpfCnpj())) {
            Join joinProcessoParte = root.join("processoPartes");
            Join joinPessoa = joinProcessoParte.join("pessoa");
            Join joinDocumentos = joinPessoa.join("pessoaDocumentos", JoinType.LEFT);
            String cpfCnpj = processoFilter.getCpfCnpj();
            Expression<String> expressionTipoDocumento = joinDocumentos.get("tipoDocumento");
            Expression<String> expressionDocumento = joinDocumentos.get("documento");
            List<String> tiposDocs = new ArrayList<>();
            tiposDocs.add("CPF");
            tiposDocs.add("CPJ");
            predicates.add(expressionTipoDocumento.in(tiposDocs));
            predicates.add(builder.equal(expressionDocumento, cpfCnpj));
        }

        if (!StringUtils.isEmpty(processoFilter.getNomeDaParte())) {
            Join joinProcessoParte = root.join("processoPartes");
            Join joinPessoa = joinProcessoParte.join("pessoa");
            String nomeParte = "%" + processoFilter.getNomeDaParte().toUpperCase() + "%";
            Expression<String> expression = joinPessoa.get("nome");
            predicates.add(builder.like(expression, nomeParte));
        }

        if (processoFilter.getPrioridadeProcessual() != 0) {
            Join join = root.join("prioridades");
            int codigo = processoFilter.getPrioridadeProcessual();
            Expression<Long> expression = join.get("id");
            predicates.add(expression.in(codigo));
        }

        if (processoFilter.getTipoDocumento() > 0) {
            Join join = root.join("minutas");
            String codigo = String.valueOf(processoFilter.getTipoDocumento());

            Predicate predicate = builder.and(
                builder.equal(join.get("tipoDocumento").as(String.class), codigo),
                builder.isNull(join.get("idMinutaSistemaLegado"))
            );

            predicates.add(predicate);
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private Predicate getPredicatesInOracleLimit(CriteriaBuilder builder, Root<Processo> root, List<Long> values){
        List<Predicate> predications = new ArrayList();
        int listSize = values.size();
        for (int i = 0; i < listSize; i += PARAMETER_LIMIT) {
            List subList;
            if (listSize > i + PARAMETER_LIMIT) {
                subList = values.subList(i, (i + PARAMETER_LIMIT));
            } else {
                subList = values.subList(i, listSize);
            }
            predications.add(root.get("id").in(subList));
        }
        return builder.or(predications.toArray(Predicate[]::new));
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<Processo> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long totalPagina(ProcessoFilter processoFilter, int caixa, List<TarefaEnum> tarefas, List<String> idOJ) {
        return totalPagina(processoFilter,caixa,tarefas,idOJ,new ArrayList<>(),new ArrayList<>());
    }

    private Long totalPagina(ProcessoFilter processoFilter, int caixa, List<TarefaEnum> tarefa, List<String> idOJ, List<Long> idProcessos, List<OrgaoJulgador> orgaoJulgadores) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Processo> root = criteria.from(Processo.class);
        Predicate[] predicates = criarRestricoes(processoFilter, builder, root, caixa, tarefa, idOJ, idProcessos, new ArrayList<>());
        criteria.where(predicates);
        criteria.select(builder.count(root));
        return manager.createQuery(criteria.distinct(true)).getSingleResult();
    }

    private Long totalPagina(Predicate[] predicates){
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Processo> root = criteria.from(Processo.class);
        criteria.where(predicates);
        criteria.select(builder.count(root));
        return manager.createQuery(criteria.distinct(true)).getSingleResult();
    }

    /**
        Correção para 'Could not locate TableGroup' ao usar mesmo predicates
     */
    private Long totalPaginaNumeroProcesso(String numeroProcesso, List<OrgaoJulgador> orgaoJulgadores){
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Processo> root = criteria.from(Processo.class);
        ProcessoFilter processoFilter = new ProcessoFilter();
        processoFilter.setNumeroProcesso(numeroProcesso);
        Predicate[] predicates = criarRestricoes(processoFilter, builder, root, 0, null, null, null,orgaoJulgadores);

        criteria.where(predicates);
        criteria.select(builder.count(root));
        return manager.createQuery(criteria.distinct(true)).getSingleResult();
    }

}
