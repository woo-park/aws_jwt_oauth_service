package com.awsjwtservice.repository;


import com.awsjwtservice.domain.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class ItemRepository {


//    private static final Logger logger = LoggerFactory.getLogger(ItemRepository.class);

    @PersistenceContext
    EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);


        } else {
            em.merge(item);
        }

//        logger.info("Saving item...");
//        logger.info(item.toString());
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i",Item.class).getResultList();
    }
}
