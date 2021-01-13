///**
// *
// */
package com.awsjwtservice.config;

import com.awsjwtservice.config.security.CustomOAuth2AuthenticationProcessingFilter;
import com.awsjwtservice.config.security.JwtAuthenticationConfigurer;
import com.awsjwtservice.config.security.JwtAuthenticationService;
//import com.awsjwtservice.config.security.OAuth2AuthenticationFilter;
//import com.awsjwtservice.config.security.OAuth2AuthenticationProvider;
//import com.awsjwtservice.config.security.OAuth2AuthenticationProvider;
//import com.awsjwtservice.config.security.OAuth2AuthenticationFilter;
import com.awsjwtservice.config.security.OAuth2UserServiceImpl;
import com.awsjwtservice.config.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//
//
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailServiceImpl userDetailsService;
//
    @Autowired
    JwtAuthenticationService jwtAuthenticationService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
	}

    private final OAuth2UserServiceImpl oAuth2UserServiceImpl;

    public SecurityConfig(OAuth2UserServiceImpl oAuth2UserServiceImpl) {
        this.oAuth2UserServiceImpl = oAuth2UserServiceImpl;
    }

//    Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient =
//                this.tokenEndpointConfig.accessTokenResponseClient;
//        if (accessTokenResponseClient == null) {
//            accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
//        }
//
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService = getOAuth2UserService();
//        OAuth2LoginAuthenticationProvider oauth2LoginAuthenticationProvider =
//                new OAuth2LoginAuthenticationProvider(accessTokenResponseClient, oauth2UserService);
//
//        auth.authenticationProvider(new OAuth2AuthenticationProvider());
//    }

//
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
            .antMatchers("/test").hasRole("USER")
            .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
			.antMatchers(HttpMethod.POST, "/oauth/token").permitAll()

			.anyRequest().authenticated()
            .and()
            .apply(new JwtAuthenticationConfigurer(jwtAuthenticationService));
//		http
//            .addFilterBefore(new CustomOAuth2AuthenticationProcessingFilter(), BasicAuthenticationFilter.class);

        http
            .oauth2Login()
//            .loginPage("/login")
            .failureHandler(new AuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                    System.out.println("authentication" + exception.getMessage());

//                    response.sendRedirect("/login");
                }
            })
            .userInfoEndpoint()     // 이것과 밑 userService는 연결되어있다
            .userService(oAuth2UserServiceImpl)
                .and()
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginFailure");


		http
			.csrf().disable()
			.httpBasic().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
//
//
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(userDetailsService)
//			.passwordEncoder(encoder());
//
//        auth.authenticationProvider(new OAuth2AuthenticationProvider(OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient, ));
//	}



	@Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
//
	@Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}
