package com.awsjwtservice.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public class testController {

    @GetMapping("/test")
    public String user(Principal principal, Model model, HttpServletRequest request) {
        System.out.println("Authorization Header Value ::"+request.getHeader("Authorization"));
//        return principal;
        return "test";
    }
}


