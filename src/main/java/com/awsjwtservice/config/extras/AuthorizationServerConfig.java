///**
// *
// */
//package com.awsjwtservice.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
///**
// * @author Dinesh.Rajput
// *
// */
//
//@Configuration
//@EnableAuthorizationServer
//public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
//
//	static final String CLIEN_ID = "dineshonjava";
//	static final String CLIENT_SECRET = "$2a$04$TJmCr9KA4dRF1Gir.zQ1TO/q9qELju1EzDpYhFBlbjxevCI7HZY5G";
//	static final String GRANT_TYPE_PASSWORD = "password";
//	static final String AUTHORIZATION_CODE = "authorization_code";
//    static final String REFRESH_TOKEN = "refresh_token";
//    static final String IMPLICIT = "implicit";
//	static final String SCOPE_READ = "read";
//	static final String SCOPE_WRITE = "write";
//    static final String TRUST = "trust";
//	static final int ACCESS_TOKEN_VALIDITY_SECONDS = 1*60*60;
//    static final int FREFRESH_TOKEN_VALIDITY_SECONDS = 6*60*60;
//	private String privateKey = "111dinesh000";
//
//	@Autowired
//	private AuthenticationManager authenticationManager;
//
//	@Autowired
//	BCryptPasswordEncoder encoder;
//
//	@Bean
//	public JwtAccessTokenConverter tokenEnhancer() {
//		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//		converter.setSigningKey(privateKey);
//		return converter;
//	}
//
//	@Bean
//	public JwtTokenStore tokenStore() {
//		return new JwtTokenStore(tokenEnhancer());
//	}
//
//	@Override
//	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
//		.accessTokenConverter(tokenEnhancer());
//	}
//
//	@Override
//	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").passwordEncoder(encoder);
//	}
//
//	@Override
//	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		clients.inMemory()
//		.withClient(CLIEN_ID)
//		.secret(CLIENT_SECRET)
//		.authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT )
//		.scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
//		.accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
//		.refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS);
//	}
//
//}
