/**
 * 
 */
package com.awsjwtservice.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JwtAuthenticationFilter extends GenericFilterBean {
	
    private JwtAuthenticationService jwtAuthenticationService;

    public JwtAuthenticationFilter(JwtAuthenticationService jwtAuthenticationService) {
    	this.jwtAuthenticationService = jwtAuthenticationService;
    }

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		Authentication authentication = jwtAuthenticationService.getAuthentication((HttpServletRequest) servletRequest);
		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

        filterChain.doFilter(servletRequest, response);
	}

}
