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

    public Page<Rounds> findAll(PrivacyType privacyType, Pageable pageable) {

        // privacyType

        List<Rounds> rounds = em.createQuery("select r from Rounds r ORDER BY r.regdate DESC",Rounds.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())  //startPosition 조회 시작 위치(0부터 시작한다)
                .setMaxResults(pageable.getPageSize())  // maxResult 조회할 데이터 수
                .getResultList();

        /*
        * 시작은 0 이므로 1번째부터 시작해서 총 10건의 데이터를 조회한다. 따라서 1~10번 데이터를 조회한다
        * */

        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > rounds.size() ? rounds.size() : (start + pageable.getPageSize());

        return new PageImpl<>(rounds.subList(start,end), pageable, rounds.size());
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
