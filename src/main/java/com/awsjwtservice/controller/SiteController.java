package com.awsjwtservice.controller;

import com.awsjwtservice.config.annotation.LoginUser;
import com.awsjwtservice.domain.Site;
import com.awsjwtservice.dto.SessionUserDto;
import com.awsjwtservice.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class SiteController {

    // get a logger
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Autowired
    SiteService siteService;

    @GetMapping("/site/create")
    public String createSite(Model model, @LoginUser SessionUserDto user) throws Exception {

        // template 으로 userSeq 넘김
        if(user != null) {
            model.addAttribute("userSeq", user.getUserSeq());
            model.addAttribute("email", user.getEmail());
        }


        return "createSite";
    }



    @GetMapping("/{siteUrl}")
    public String getSite(@PathVariable("siteUrl") String siteUrl, Model model) {

        Optional<Site> site = siteService.findBySiteUrl(siteUrl);
        if(site.isPresent()) {

            model.addAttribute("title", site.get().getTitle());

            return "site";
        } else {
            return "redirect:/sites";
        }

//        return siteService.find(Long.parseLong(siteSeq)).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
