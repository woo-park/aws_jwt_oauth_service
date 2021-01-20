
//package com.awsjwtservice.config.security;
//
//import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
////import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationExchangeValidator;
//import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
//import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
//import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
//import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
//import org.springframework.security.oauth2.core.OAuth2Error;
//import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.util.Assert;
//import org.springframework.util.ObjectUtils;
//
//import java.util.Collection;
//import java.util.Map;
//
//
//
////public class OAuth2AuthenticationProvider implements AuthenticationProvider {
////	private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;
////	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> userService;
////	private GrantedAuthoritiesMapper authoritiesMapper = (authorities -> authorities);
////
////	public OAuth2AuthenticationProvider(
////			OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient,
////			OAuth2UserService<OAuth2UserRequest, OAuth2User> userService) {
//////		super(accessTokenResponseClient, userService);
////
////		Assert.notNull(accessTokenResponseClient, "accessTokenResponseClient cannot be null");
////		Assert.notNull(userService, "userService cannot be null");
////		this.accessTokenResponseClient = accessTokenResponseClient;
////		this.userService = userService;
////	}
////
////	@Override
////	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
////		OAuth2LoginAuthenticationToken authorizationCodeAuthentication =
////				(OAuth2LoginAuthenticationToken) authentication;
////
////		if (authorizationCodeAuthentication.getAuthorizationExchange()
////				.getAuthorizationRequest().getScopes().contains("openid")) {
////			return null;
////		}
////
////		OAuth2AccessTokenResponse accessTokenResponse;
////		try {
//////			OAuth2AuthorizationExchangeValidator.validate(
//////					authorizationCodeAuthentication.getAuthorizationExchange());
////
////			accessTokenResponse = this.accessTokenResponseClient.getTokenResponse(
////					new OAuth2AuthorizationCodeGrantRequest(
////							authorizationCodeAuthentication.getClientRegistration(),
////							authorizationCodeAuthentication.getAuthorizationExchange()));
////
////		} catch (OAuth2AuthorizationException ex) {
////			OAuth2Error oauth2Error = ex.getError();
////			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
////		}
////
////		OAuth2AccessToken accessToken = accessTokenResponse.getAccessToken();
////		Map<String, Object> additionalParameters = accessTokenResponse.getAdditionalParameters();
////
////		OAuth2User oauth2User = this.userService.loadUser(new OAuth2UserRequest(
////				authorizationCodeAuthentication.getClientRegistration(), accessToken, additionalParameters));
////
////		Collection<? extends GrantedAuthority> mappedAuthorities =
////				this.authoritiesMapper.mapAuthorities(oauth2User.getAuthorities());
////
////		System.out.println("will this print out token");
////		OAuth2LoginAuthenticationToken authenticationResult = new OAuth2LoginAuthenticationToken(
////				authorizationCodeAuthentication.getClientRegistration(),
////				authorizationCodeAuthentication.getAuthorizationExchange(),
////				oauth2User,
////				mappedAuthorities,
////				accessToken,
////				accessTokenResponse.getRefreshToken());
////		authenticationResult.setDetails(authorizationCodeAuthentication.getDetails());
////
////		return authenticationResult;
////	}
////
////
//////	public final void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
//////		Assert.notNull(authoritiesMapper, "authoritiesMapper cannot be null");
//////		this.authoritiesMapper = authoritiesMapper;
//////	}
////
////	@Override
////	public boolean supports(Class<?> authentication) {
////		return OAuth2LoginAuthenticationToken.class.isAssignableFrom(authentication);
////	}
////}
//
////
//public class OAuth2AuthenticationProvider implements AuthenticationProvider {
//
//
//	@Override
//	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//		OAuth2AuthenticationToken auth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
//		String accessToken = auth2AuthenticationToken.getToken();
////
////		OAuth2UserInfoTokenServices oAuth2UserInfoTokenServices = new OAuth2UserInfoTokenServices(
////				resourceServerProperties.getUserInfoUri(), resourceServerProperties.getClientId());
////
////		OAuth2Authentication oAuth2Authentication = oAuth2UserInfoTokenServices.loadAuthentication(accessToken);
////
////		if (ObjectUtils.isEmpty(oAuth2Authentication)) {
////			return null;
////		}
////		oAuth2Authentication.setAuthenticated(true);
////		return oAuth2Authentication;
//        return null;
//	}
//
//
//
//	@Override
//	public boolean supports(Class<?> authentication) {
//		return OAuth2AuthenticationToken.class.isAssignableFrom(authentication);
//	}
//}
