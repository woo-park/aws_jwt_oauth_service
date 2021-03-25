package com.awsjwtservice.controller;

import com.awsjwtservice.config.annotation.LoginUser;
import com.awsjwtservice.domain.Site;
import com.awsjwtservice.dto.SessionUserDto;
import com.awsjwtservice.service.SiteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class SiteController {

    @GetMapping("/site/create")
    public String createSite(Model model, @LoginUser SessionUserDto user) throws Exception {
        if(user != null) {
            model.addAttribute("userSeq", user.getUserSeq());
            model.addAttribute("email", user.getEmail());
        }

        return "createSite";
    }
}
