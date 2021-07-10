package com.awsjwtservice.config;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//import com.awsjwtservice.config.oauth2request.CustomAuthorizationRequestResolver;
import com.awsjwtservice.config.annotation.LoginUserHandlerMethodArgumentResolver;
import com.awsjwtservice.config.formlogin.FormAccessDeniedHandler;
import com.awsjwtservice.config.formlogin.FormAuthenticationProvider;
import com.awsjwtservice.config.formlogin.FormSuccessHandler;
import com.awsjwtservice.config.oauth2request.CustomOAuth2Provider;
import com.awsjwtservice.config.oauth2request.CustomRequestEntityConverter;
import com.awsjwtservice.config.oauth2request.CustomTokenResponseConverter;
import com.awsjwtservice.config.security.OAuth2UserServiceImpl;
import com.awsjwtservice.config.service.UserDetailServiceImpl;
import com.awsjwtservice.domain.Site;
import com.awsjwtservice.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

//@Configuration
@Configuration
@EnableWebSecurity(debug = true)
//@PropertySource("classpath:application-oauth.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailServiceImpl userDetailsService;




    // 필터 건더뛰기
    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    public SecurityConfig(OAuth2UserServiceImpl oAuth2UserServiceImpl) {
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


    public AccessDeniedHandler accessDeniedHandler() {
        FormAccessDeniedHandler commonAccessDeniedHandler = new FormAccessDeniedHandler();
        commonAccessDeniedHandler.setErrorPage("/denied");
        return commonAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider () {
        return new FormAuthenticationProvider(passwordEncoder());
    }

    @Autowired
    private AuthenticationSuccessHandler formAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler formAuthenticationFailureHandler;


    @Autowired
    private SiteRepository siteRepository;
    @Override
    protected void configure(HttpSecurity http) throws Exception {



        matchUrlAndAuthority(http);

        http.csrf().disable()
                .headers().frameOptions().disable();


        http.authorizeRequests()
                .antMatchers("/css/base.css").permitAll()
                .antMatchers("/css/style.css").permitAll()
                .antMatchers("/css/login.css").permitAll()

                .antMatchers("/denied").permitAll()
                .antMatchers("/oauth_login","/oauth_login/**","/login_proc", "/oauth2/authorize-client","/oauth2/authorize-client/**", "/loginSuccess", "/loginFailure", "/loginSuccess/**", "/loginFailure/**", "/", "/h2-console", "/h2-console/**").permitAll()
                .antMatchers("/register","/register/**").permitAll()
                .antMatchers("/test").hasRole("USER")
                .antMatchers("/mypage").access("hasRole('ADMIN') or hasRole('USER') or hasRole('MANAGER')")
                .antMatchers( "/forgotPwd","/resetPwd").permitAll()

                .antMatchers("/files", "/files/**").permitAll()
                .antMatchers(HttpMethod.POST, "/files").permitAll()

                .antMatchers("/gallery", "/gallery/**","/s3basket").permitAll()
                .antMatchers(HttpMethod.POST, "/gallery", "/gallery/upload").permitAll()

                .antMatchers("/sites", "/sites/**","/site/create").permitAll()
                .antMatchers(HttpMethod.POST, "/sites", "/sites/**").permitAll()

                .antMatchers("/users/export/pdf").permitAll()

//                .antMatchers("/**").permitAll()

                .antMatchers(HttpMethod.POST, "/login_proc").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .antMatchers(HttpMethod.POST, "/oauth/token").permitAll()
                .antMatchers("/js/p5/**").permitAll()
                .antMatchers("/js/main/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/objects/**").permitAll()
                .antMatchers("/objects/paperplane/**").permitAll()
                .anyRequest().access("@authorizationChecker.check(request, authentication)");
//                .authenticated();

        http    .formLogin()
                .loginPage("/oauth_login")
                .loginProcessingUrl("/login_proc")
//                .authenticationDetailsSource(formAuthenticationDetailsSource)
                .successHandler(new FormSuccessHandler())
                .failureHandler(formAuthenticationFailureHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
                .exceptionHandling()
//                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login2"));
                .accessDeniedPage("/denied")
                .accessDeniedHandler(accessDeniedHandler());

        http
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
                .failureUrl("/loginFailure");

        http
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("SESSION", "JSESSIONID", "Jwt", "Session")
                .invalidateHttpSession(true).permitAll();

        http
                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionFixation().changeSessionId()    // servlet 3.1 이상은 기본으로 changeSessionId invoked되지만, custom할수있다 ( none, migrateSession <- 3.1이하 , newSession 으로  // 세션 고정 공격을 막기위해 cookie session id값을 바꿔줘야한다
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false) //default는 false    // true는 login을 아예 못하게 만드는 전략   // false는 이전session에서 더이상 활동못하게 막는 전략
        ;
    }

    private void matchUrlAndAuthority(HttpSecurity http) throws Exception {
        List<Site> urlMatchers = siteRepository.findAll();
        for (Site matcher : urlMatchers) {
            http
                    .authorizeRequests()
                    .antMatchers("/" + matcher.getSiteUrl()).permitAll();//.hasAuthority(matcher.getAuthority()



        }
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