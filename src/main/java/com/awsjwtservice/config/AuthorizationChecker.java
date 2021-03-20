package com.awsjwtservice.config;


import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.Site;
import com.awsjwtservice.repository.SiteRepository;
import com.awsjwtservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthorizationChecker {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SiteRepository siteRepository;


    public boolean check(HttpServletRequest request, Authentication authentication) {
        Object principalObj = authentication.getPrincipal();

//        if (!(principalObj instanceof Account)) {
//            return false;
//        }

        String authority = null;
        String siteUrl = "";
        for (Site matcher : siteRepository.findAll()) {
            String url = matcher.getSiteUrl();

            System.out.println("test");
            System.out.println(url);
            System.out.println(request.getRequestURI());
            if (new AntPathMatcher().match("/" + url, request.getRequestURI())) {
//                authority = matcher.getAuthority();
                siteUrl = matcher.getSiteUrl();
                break;
            }
        }

        if(siteUrl == "") {
            return false;
        }
//        String userId = ((User) authentication.getPrincipal()).getUserId();
//        User loggedUser = userRepository.findByUserId(userId);

//        List<String> authorities = loggedUser.getAuthority();

//        if (authority == null || !authorities.contains(authority)) {
//            return false;
//        }
        return true;
    }
}
