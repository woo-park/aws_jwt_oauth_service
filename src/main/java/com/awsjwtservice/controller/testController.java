package com.awsjwtservice.controller;

import com.awsjwtservice.dto.SessionUserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;


@Controller
public class testController {
    private final HttpSession httpSession;

    public testController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }


    @GetMapping("/test")
    public String user(Principal principal, Model model, HttpServletRequest request) {
        System.out.println("Authorization Header Value ::" + request.getHeader("Authorization"));
//        return principal;


        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");

        if(user != null){
            model.addAttribute("userName", user.getUsername());
        }


        return "test";
    }
}


