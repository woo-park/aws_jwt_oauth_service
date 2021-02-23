package com.awsjwtservice.service;

import com.awsjwtservice.domain.Site;

import java.util.Optional;

public interface SiteService {
    Iterable<Site> findAll();

    Site create(Site site);
    Optional<Site> find(long id);

    void deleteAll();
}
