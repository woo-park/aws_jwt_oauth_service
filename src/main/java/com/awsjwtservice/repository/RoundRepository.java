package com.awsjwtservice.repository;


import com.awsjwtservice.domain.*;
import com.awsjwtservice.domain.item.Item;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class RoundRepository {

    @PersistenceContext
    EntityManager em;

    public void save(Rounds round) {
        em.persist(round);
    }

    public Rounds findOne(Long id) {
        return em.find(Rounds.class, id);
    }

    public Page<Rounds> findAll(String privacyStatus, Pageable pageable) {
        List<Rounds> rounds = em.createQuery("select i from Rounds i",Rounds.class)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();


        int pageSize = pageable.getPageSize();
        long pageOffset = pageable.getOffset();
//        long total = pageOffset + rounds.size() + (rounds.size() == pageSize ? pageSize : 0);
        Page<Rounds> pages = new PageImpl<Rounds>(rounds, pageable,total);

//        PagedListHolder pages = new PagedListHolder(rounds);
//        pages.setPageSize(pageSize); // number of items per page
//        pages.setPage(pageable.getPageNumber());      // set to first page

        return pages;
    }
    public List<Rounds> findAll(Long accountId) {

        TypedQuery<Rounds> query = em.createQuery("select i from Rounds i where i.account.id = :accountId",Rounds.class);

        List<Rounds> rounds = query
                .setParameter("accountId", accountId)
                .getResultList();

        return rounds;
    }

    public Rounds findLatestRound(long accountId) {

        TypedQuery<Rounds> query = em.createQuery("select i from Rounds i where i.account.id = :accountId order by i.regdate desc", Rounds.class);

        List<Rounds> rounds = query
                .setParameter("accountId", accountId)
                .setMaxResults(5)
                .getResultList();

        return rounds.get(0);
    }

//    public List<Rounds> findAll(Long accountId) {
//
//        TypedQuery<Rounds> query = em.createQuery(
//                "SELECT e.accountId FROM Rounds as e WHERE e.accountId = :accountId" , Rounds.class);
//
//
//        List<Rounds> rounds = query
//                .setParameter("accountId", accountId)
//                .getResultList();
//
//        return rounds;
//    }


//    public List<?> findAll() {
//        List<?> rounds = em.createQuery("SELECT rounds from rounds where rounds.accountId = ?1")
//                .setParameter(1, "4")
//                .getResultList();
//        return rounds;
//    }
//    public List<Rounds> findAll(RoundSearch roundSearch) {
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Rounds> cq = cb.createQuery(Rounds.class);
//        Root<Rounds> o = cq.from(Rounds.class);
//
//        List<Predicate> criteria = new ArrayList<Predicate>();
//
//        //회원 이름 검색
//        if (StringUtils.hasText(roundSearch.getMemberName())) {
//            Join<Rounds, Account> m = o.join("account", JoinType.INNER); //회원과 조인
//            Predicate name = cb.like(m.<String>get("username"), "%" + roundSearch.getMemberName() + "%");
//            criteria.add(name);
//        }
//
//        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
//        TypedQuery<Rounds> query = em.createQuery(cq).setMaxResults(100); //최대 검색 100 건으로 제한
//        return query.getResultList();
//
//    }


}
