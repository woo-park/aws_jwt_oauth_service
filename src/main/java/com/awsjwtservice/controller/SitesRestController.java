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
import java.util.List;
import java.util.Optional;

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
//        System.out.println(newSiteDto +"newSiteDto!!");
        try {
            Optional<Site> siteInfo = siteService.findBySiteUrl(newSiteDto.getSiteUrl());
            if(siteInfo.isPresent()) {
//                System.out.println("already exists!");
                return ResponseEntity.badRequest().build();
            } else {
                String title = newSiteDto.getTitle().trim();
                String siteUrl = newSiteDto.getSiteUrl().replaceAll("\\s+","");

                Site created = siteService.create(Site.builder().userSeq(Long.parseLong(newSiteDto.getUserSeq())).title(title).siteUrl(siteUrl).build());
                URI newSiteUri = uriBuilder.path("/sites/{siteSeq}").build(created.getId());
                return ResponseEntity.created(newSiteUri).body(created);

                /*
                    delCheck: 0
                    id: 5
                    regdate: "2021-05-23T01:33:20.821256"
                    siteUrl: "wavy"
                    title: "wavy"
                    uptdate: "2021-05-23T01:33:20.822947"
                    userSeq: 4
                * */
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }


//        return (ResponseEntity<Site>) ResponseEntity.ok();
    }


}
