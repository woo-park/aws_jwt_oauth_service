package com.awsjwtservice.repository;


import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.OrderSearch;
import com.awsjwtservice.domain.Orders;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: HolyEyE
 * Date: 2013. 12. 3. Time: 오후 10:28
 */
@Repository
public class OrderRepository {

    @PersistenceContext
    EntityManager em;

    public void save(Orders order) {
        em.persist(order);
    }

    public Orders findOne(Long id) {
        return em.find(Orders.class, id);
    }

    public List<Orders> findAll(OrderSearch orderSearch) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> o = cq.from(Orders.class);

        List<Predicate> criteria = new ArrayList<Predicate>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Join<Orders, Account> m = o.join("member", JoinType.INNER); //회원과 조인
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Orders> query = em.createQuery(cq).setMaxResults(1000); //최대 검색 1000 건으로 제한
        return query.getResultList();

    }
}
