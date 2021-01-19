package com.awsjwtservice.config.formlogin;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FormAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {

        setDefaultTargetUrl("/");

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if(savedRequest!=null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}




// securityConfig 직접 입력시 이렇게 successHandler을 작성할수 있습니다.

/*
    .successHandler(new AuthenticationSuccessHandler() {        // 강의 12)
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            RequestCache requestCache = new HttpSessionRequestCache();       // 이 class를 활용해서
            RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            SavedRequest savedRequest = requestCache.getRequest(request, response); // 원래 사용자가 가고자하던 그 url정보를 가지고있습니다.

            if(savedRequest != null) {
                String targetUrl = savedRequest.getRedirectUrl();
                redirectStrategy.sendRedirect(request, response, targetUrl);
            } else {
                redirectStrategy.sendRedirect(request, response, "/");
            }

        }
    })
*/