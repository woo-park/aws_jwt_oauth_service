package com.awsjwtservice.controller;

import com.awsjwtservice.domain.Site;
import com.awsjwtservice.service.SiteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/sites")
public class SiteRestController {
    private final SiteService siteService;

    @GetMapping("/{siteUrl}")
    public ResponseEntity<Site> getSite(@PathVariable("siteUrl") String siteUrl) {

        return siteService.findBySiteUrl(siteUrl).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//        return siteService.find(Long.parseLong(siteSeq)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Iterable<Site> all() {
        return siteService.findAll();
    }



    @GetMapping("/{siteSeq}")
    public ResponseEntity<Site> get(@PathVariable("siteSeq") String siteSeq) {
        return siteService.find(Long.parseLong(siteSeq)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Site> create(@RequestParam(value = "userSeq", required = true) String userSeq,@RequestParam(value = "title", required = true) String title,@RequestParam(value = "siteUrl", required = true) String siteUrl, UriComponentsBuilder uriBuilder) {

//        System.out.println(site +"site!!");

        Site created = siteService.create(Site.builder().userSeq(Long.parseLong(userSeq)).title(title).siteUrl(siteUrl).build());
        URI newSiteUri = uriBuilder.path("/sites/{siteSeq}").build(created.getId());

        return ResponseEntity.created(newSiteUri).body(created);
    }

    // scartch test worked
//    @PostMapping
//    public ResponseEntity<Site> create(@RequestBody Site site, UriComponentsBuilder uriBuilder) {
//
//        System.out.println(site +"site!!");
//        Site created = siteService.create(site);
//        URI newSiteUri = uriBuilder.path("/sites/{siteSeq}").build(created.getId());
//
//        return ResponseEntity.created(newSiteUri).body(created);
//    }

}
