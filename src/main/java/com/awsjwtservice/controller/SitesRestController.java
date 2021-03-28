package com.awsjwtservice.controller;

import com.awsjwtservice.domain.Site;
import com.awsjwtservice.dto.SiteDto;
import com.awsjwtservice.service.SiteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/sites")
public class SitesRestController {
    private final SiteService siteService;

    private final ObjectMapper objectMapper;
    @GetMapping
    public Iterable<Site> all() {
        return siteService.findAll();
    }



    @GetMapping("/{siteSeq}")
    public ResponseEntity<Site> get(@PathVariable("siteSeq") String siteSeq) {
        return siteService.find(Long.parseLong(siteSeq)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

//    @PostMapping
//    public ResponseEntity<Site> create(@RequestBody Site site, UriComponentsBuilder uriBuilder) {
//
//        Site created = siteService.create(site);
//        URI newSiteUri = uriBuilder.path("/sites/{siteSeq}").build(created.getId());
//
//        return ResponseEntity.created(newSiteUri).body(created);
//    }


    @PostMapping
    public ResponseEntity<Site> create(
            @RequestBody String siteDto,
            UriComponentsBuilder uriBuilder) throws JsonProcessingException {


        SiteDto newSiteDto = objectMapper.readValue(siteDto, SiteDto.class);
        System.out.println(newSiteDto +"newSiteDto!!");

        Site created = siteService.create(Site.builder().userSeq(Long.parseLong(newSiteDto.getUserSeq())).title(newSiteDto.getTitle()).siteUrl(newSiteDto.getSiteUrl()).build());
        URI newSiteUri = uriBuilder.path("/sites/{siteSeq}").build(created.getId());
//
        return ResponseEntity.created(newSiteUri).body(created);
//        return (ResponseEntity<Site>) ResponseEntity.ok();
    }


}
