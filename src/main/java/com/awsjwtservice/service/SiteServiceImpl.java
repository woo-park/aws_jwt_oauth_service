package com.awsjwtservice.service;

import com.awsjwtservice.domain.Site;
import com.awsjwtservice.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SiteServiceImpl implements SiteService{

    @Autowired
    SiteRepository siteRepository;

    @Override
    public Iterable<Site> findAll() {
        return siteRepository.findAll();
    }

    @Override
    public Site create(Site site) {
//        Site resultSite = siteRepository.findById(site.getUserSeq());
//        if(resultSite.)

        return siteRepository.save(site);
    }

    @Override
    public Optional<Site> find(long siteSeq) {
        return Optional.ofNullable(siteRepository.findById(siteSeq));
    }

    @Override
    public void deleteAll() {
        siteRepository.deleteAll();
    }
}
