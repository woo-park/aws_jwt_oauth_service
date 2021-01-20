package com.awsjwtservice.config.formlogin;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.dto.SessionUserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

        Account authenticatedUser = (Account) authentication.getPrincipal();

        session.setAttribute("user", SessionUserDto.builder().username(authenticatedUser.getUsername()).email(authenticatedUser.getEmail()).build());
        session.setAttribute("test", "test string");
        // end?
        response.sendRedirect("/mypage");

    }
}
