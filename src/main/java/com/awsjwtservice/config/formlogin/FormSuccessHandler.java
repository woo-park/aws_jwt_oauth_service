package com.awsjwtservice.config.formlogin;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.dto.AccountDto;
import com.awsjwtservice.dto.SessionUserDto;
//import com.awsjwtservice.dto.UserDetails;
import org.springframework.security.core.Authentication;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

public class FormSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();

//        Object authenticatedUser = authentication.getPrincipal();
//        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
        Account authenticatedUser = (Account) authentication.getPrincipal();


        session.setAttribute("user", SessionUserDto.builder().role(authenticatedUser.getRole()).username(authenticatedUser.getUsername()).email(authenticatedUser.getEmail()).userSeq(authenticatedUser.getId()).build());
        session.setAttribute("test", "test string");


        if (session != null) {
            String redirectUrl = (String) session.getAttribute("prevPage");
            if (redirectUrl != null) {
                session.removeAttribute("prevPage");
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect("/");

            }
        }
        // end?
        response.sendRedirect("/mypage");

    }
}
