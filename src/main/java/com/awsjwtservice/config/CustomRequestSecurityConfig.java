package com.awsjwtservice.config;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//import com.awsjwtservice.config.oauth2request.CustomAuthorizationRequestResolver;
import com.awsjwtservice.config.oauth2request.CustomOAuth2Provider;
import com.awsjwtservice.config.oauth2request.CustomRequestEntityConverter;
import com.awsjwtservice.config.oauth2request.CustomTokenResponseConverter;
import com.awsjwtservice.config.security.JwtAuthenticationConfigurer;
import com.awsjwtservice.config.security.JwtAuthenticationService;
import com.awsjwtservice.config.security.OAuth2UserServiceImpl;
import com.awsjwtservice.config.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

//@Configuration
@Configuration
@EnableWebSecurity
@PropertySource("classpath:application-oauth.properties")
public class CustomRequestSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailServiceImpl userDetailsService;

    public CustomRequestSecurityConfig(OAuth2UserServiceImpl oAuth2UserServiceImpl) {
        this.oAuth2UserServiceImpl = oAuth2UserServiceImpl;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
	}

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    private final OAuth2UserServiceImpl oAuth2UserServiceImpl;

//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(dataSource);
//    }

    @Autowired
    JwtAuthenticationService jwtAuthenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/oauth_login", "/loginFailure", "/")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .oauth2Login()
//                .loginPage("/oauth_login")
//                .authorizationEndpoint()
//                .authorizationRequestResolver( new CustomAuthorizationRequestResolver(clientRegistrationRepository(),"/oauth2/authorize-client"))
//
//                .baseUri("/oauth2/authorize-client")
//                .authorizationRequestRepository(authorizationRequestRepository())
//                .and()
//                .tokenEndpoint()
//                .accessTokenResponseClient(accessTokenResponseClient())
//                .and()
//                .defaultSuccessUrl("/loginSuccess")
//                .failureUrl("/loginFailure");
        http.csrf().disable()
                .headers().frameOptions().disable();




        http.authorizeRequests()
                .antMatchers("/oauth_login", "/loginFailure", "/", "/h2-console", "/h2-console/**")
                .permitAll()

                .antMatchers("/test").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .antMatchers(HttpMethod.POST, "/oauth/token").permitAll()

                .anyRequest()
                .authenticated()
                .and()
                .oauth2Login()
                .loginPage("/oauth_login")
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize-client")
                .authorizationRequestRepository(authorizationRequestRepository())
                .and()
                .tokenEndpoint()
                .accessTokenResponseClient(accessTokenResponseClient())
                .and()
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginFailure")
                ;
//                .and()
//                .apply(new JwtAuthenticationConfigurer(jwtAuthenticationService));


    }




//    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }

//    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        accessTokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter());

        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setTokenResponseConverter(new CustomTokenResponseConverter());
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        accessTokenResponseClient.setRestOperations(restTemplate);
        return accessTokenResponseClient;
    }


    // additional configuration for non-Spring Boot projects
    private static List<String> clients = Arrays.asList("google");

//    //@Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        List<ClientRegistration> registrations = clients.stream()
//                .map(c -> getRegistration(c))
//                .filter(registration -> registration != null)
//                .collect(Collectors.toList());
//
//        return new InMemoryClientRegistrationRepository(registrations);
//
//    }

    public ClientRegistrationRepository clientRegistrationRepository(
            OAuth2ClientProperties oAuth2ClientProperties,
            @Value("${custom.oauth2.kakao.client-id}") String kakaoClientId,
            @Value("${custom.oauth2.kakao.client-secret}") String kakaoClientSecret,
            @Value("${custom.oauth2.naver.client-id}") String naverClientId,
            @Value("${custom.oauth2.naver.client-secret}") String naverClientSecret) {
        List<ClientRegistration> registrations = oAuth2ClientProperties
                .getRegistration()
                .keySet().stream()
                .map(client -> getRegistration(oAuth2ClientProperties, client))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        registrations.add(
                CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                .clientId(kakaoClientId)
                .clientSecret(kakaoClientSecret)
                .jwkSetUri("temp")
                .build()
        );
        registrations.add(
                CustomOAuth2Provider.NAVER.getBuilder("naver")
                .clientId(naverClientId)
                .clientSecret(naverClientSecret)
                .jwkSetUri("temp")
                .build()
        );

        return new InMemoryClientRegistrationRepository(registrations);
    }


    private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

    @Autowired
    private Environment env;

    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String client) {

        String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");

        if (clientId == null) {
            return null;
        }

        String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
        if (client.equals("google")) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("google");

            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email", "profile")
                    .build();

        }
        if (client.equals("facebook")) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("facebook");

            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link")
                    .scope("email")
                    .build();

        }
        return null;
    }
}