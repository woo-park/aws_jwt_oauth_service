package com.awsjwtservice.controller;

import com.awsjwtservice.config.annotation.LoginUser;
import com.awsjwtservice.dto.SessionUserDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;


@Controller
public class TestController {
    private final HttpSession httpSession;

    public TestController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }


    @GetMapping("/test")
//    @PreAuthorize("hasRole('ROLE_USER') and #account.username == principal.username")
    public String user(Principal principal, Model model, HttpServletRequest request, @LoginUser SessionUserDto loginUser) {
        System.out.println("Authorization Header Value ::" + request.getHeader("Authorization"));
//        return principal;


        System.out.println(loginUser + ":login user");
        System.out.println(principal + ": principal");
        System.out.println(loginUser.getUsername() + ":login user name thru loginUser annotation");
        SessionUserDto user = (SessionUserDto) httpSession.getAttribute("user");
//        결국 httpSession 에서 가져오나, @LoginUser annotation 으로 부터 가져온 dto 나 같은것이다.
        System.out.println(loginUser.getUserSeq()+":userSeq");

        if(user != null){
            model.addAttribute("name", user.getUsername());
            model.addAttribute("useremail", user.getEmail());
        }


        return "test";
    }
}


