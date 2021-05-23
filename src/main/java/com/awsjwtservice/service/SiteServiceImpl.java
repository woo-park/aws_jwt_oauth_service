package com.awsjwtservice.service;

import com.awsjwtservice.domain.Site;
import com.awsjwtservice.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
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
        // ensure site url doesn't exists
        Site siteInfo = siteRepository.findBySiteUrl(site.getSiteUrl());
        if (siteInfo == null) {
            return siteRepository.save(site);
        } else {
            return  null;
        }
    }

    @Override
    public Optional<Site> find(long siteSeq) {
        return Optional.ofNullable(siteRepository.findById(siteSeq));
    }

    @Override
    public void deleteAll() {
        siteRepository.deleteAll();
    }

    @Override
    public Optional<Site> findBySiteUrl(String siteUrl) { return Optional.ofNullable(siteRepository.findBySiteUrl(siteUrl)); }

}
