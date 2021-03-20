package com.awsjwtservice.repository;

import com.awsjwtservice.domain.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
//    @Override
    List<Gallery> findAllByDelCheck(int delCheck);

//    List<Gallery> findByIdAndDelCheckAndOrOrderByRegdate(int i);
}
