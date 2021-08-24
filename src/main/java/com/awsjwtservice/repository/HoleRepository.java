package com.awsjwtservice.repository;


import com.awsjwtservice.domain.Holes;
import com.awsjwtservice.domain.Rounds;
import com.awsjwtservice.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//@Repository
//public class HoleRepository {
//
//    @PersistenceContext
//    EntityManager em;
//
//    public void save(Holes hole) {
//        em.persist(hole);
//    }
//}



@Repository
public interface HoleRepository extends JpaRepository<Holes, Long> {

//    Site findByUserSeq(long userSeq);
//    Site findById(long siteSeq);
//    Site findBySiteUrl(String siteUrl);

}