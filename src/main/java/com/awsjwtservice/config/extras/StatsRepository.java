//package com.awsjwtservice.domain;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//
//import java.util.List;
//
//public interface StatsRepository extends CrudRepository<Stats, String> {
//
//    // Fetch products by brand
//    @Query("select p from Stats p where p.brand = ?1")
//    List<Stats> findByBrand(String brand);
//
//    // Fetch products by name and type
//    @Query("select p from Stats p where p.name = ?1 and p.type = ?2")
//    List<Stats> findByNameAndType(String name, String type);
//
//}
