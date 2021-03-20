package com.awsjwtservice.controller;

import com.awsjwtservice.domain.Site;
import com.awsjwtservice.service.SiteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class SiteController {
    private final SiteService siteService;

    @GetMapping("/{siteUrl}")
    public ResponseEntity<Site> getSite(@PathVariable("siteUrl") String siteUrl) {

        return siteService.findBySiteUrl(siteUrl).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//        return siteService.find(Long.parseLong(siteSeq)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
