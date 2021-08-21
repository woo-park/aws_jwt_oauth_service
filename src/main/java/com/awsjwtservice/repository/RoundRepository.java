package com.awsjwtservice.repository;


import com.awsjwtservice.domain.*;
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

    public List<Rounds> findAll(RoundSearch roundSearch) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rounds> cq = cb.createQuery(Rounds.class);
        Root<Rounds> o = cq.from(Rounds.class);

        List<Predicate> criteria = new ArrayList<Predicate>();

        //회원 이름 검색
        if (StringUtils.hasText(roundSearch.getMemberName())) {
            Join<Rounds, Account> m = o.join("account", JoinType.INNER); //회원과 조인
            Predicate name = cb.like(m.<String>get("username"), "%" + roundSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Rounds> query = em.createQuery(cq).setMaxResults(100); //최대 검색 100 건으로 제한
        return query.getResultList();

    }


}
