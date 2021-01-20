//package com.awsjwtservice.config.security;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.GenericFilterBean;
//
//public class OAuth2AuthenticationFilter extends GenericFilterBean {
//
//	private static final String AUTHORIZATION = "Authorization";
//	private static final String PREFIX = "Bearer";
//	private static final String EMPTY = "";
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
//			throws IOException, ServletException {
//
//		 String accessToken = ((HttpServletRequest) request).getHeader(AUTHORIZATION);
//		 if (accessToken != null && accessToken.startsWith(PREFIX)) {
//			 accessToken=  accessToken.replace(PREFIX, EMPTY).trim();
//		 }
//		 if(!StringUtils.isEmpty(accessToken)) {
//
//
////             Object authenticationDetails = this.authenticationDetailsSource.buildDetails(request);
////             OAuth2LoginAuthenticationToken authenticationRequest = new OAuth2LoginAuthenticationToken(
////                     clientRegistration, new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse));
////             authenticationRequest.setDetails(authenticationDetails);
//
////             OAuth2LoginAuthenticationToken authenticationResult =
////                     (OAuth2LoginAuthenticationToken) this.getAuthenticationManager().authenticate(authenticationRequest);
////
////             OAuth2AuthenticationToken oauth2Authentication = new OAuth2AuthenticationToken(
////                     authenticationResult.getPrincipal(),
////                     authenticationResult.getAuthorities(),
////                     authenticationResult.getClientRegistration().getRegistrationId());
//////             oauth2Authentication.setDetails(authenticationDetails);
////
//////             OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(
//////                     authenticationResult.getClientRegistration(),
//////                     oauth2Authentication.getName(),
//////                     authenticationResult.getAccessToken(),
//////                     authenticationResult.getRefreshToken());
//////
//////             this.authorizedClientRepository.saveAuthorizedClient(authorizedClient, oauth2Authentication, request, response);
////
////
////			 Authentication auth = new OAuth2AuthenticationToken(accessToken);
////			 SecurityContextHolder.getContext().setAuthentication(auth);
//		 }
//		 filterChain.doFilter(request, response);
//	}
//
//}