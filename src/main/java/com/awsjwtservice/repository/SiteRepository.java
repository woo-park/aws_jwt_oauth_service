package com.awsjwtservice.repository;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.LoginProvider;
import com.awsjwtservice.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    Site findByUserSeq(long userSeq);
    Site findById(long siteSeq);
    Site findBySiteUrl(String siteUrl);
}