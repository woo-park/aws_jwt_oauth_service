package com.awsjwtservice.controller;

import java.security.Principal;
import java.util.*;

import com.awsjwtservice.config.security.JwtAuthenticationService;
import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private static final String authorizationRequestBaseUri = "oauth2/authorize-client";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtAuthenticationService jwtAuthenticationService;


    @GetMapping("/oauth_login")
    public String getLoginPage(Principal principal, Model model, HttpServletRequest request) {
//        Iterable<ClientRegistration> clientRegistrations = null;
//        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
//                .as(Iterable.class);
//        if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
//            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
//        }
//
//        clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(), authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
//
//        for(String each : oauth2AuthenticationUrls.values()) {
//            System.out.println(each);
//        }
//        model.addAttribute("urls", oauth2AuthenticationUrls);
        System.out.println("Authorization Header Value ::" + request.getHeader("Authorization") + request.getHeader("JwtAuthorization"));
        return "oauthLogin";
    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication, HttpServletResponse servletResponse) {

//        String username = authentication.getName();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

        ClientRegistrationRepository clientRegistrationRepository;

        Object userDetailsTest = (Object)authentication.getPrincipal();

        Object currentAuth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        UserDetails principal = null;
//
//        principal = (UserDetails) currentAuth;
//        authentication.getDetails();
        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();

        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            System.out.println(HttpHeaders.AUTHORIZATION + ": HttpHeaders.AUTHORIZATION");
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                    .getTokenValue());


            String username = null;
            if (authentication.getAuthorizedClientRegistrationId().equals("naver")) {
                System.out.println("naver");
                username = (String) authentication.getPrincipal().getAttributes().get("name");
            } else if (authentication.getAuthorizedClientRegistrationId().equals("kakao") ) {
                Map<String, Object> details = (Map<String, Object>) authentication.getPrincipal().getAttributes();
//                final Map<?, ?> modifiable = new HashMap<>(details);

//                username = (String)((HashMap)details.get("properties")).get("nickname");
                username = (String)((HashMap)((HashMap)details.get("kakao_account")).get("profile")).get("nickname");
            } else if (authentication.getAuthorizedClientRegistrationId().equals("google") ) {
                System.out.println("google");
                username = (String) authentication.getPrincipal().getAttributes().get("name");
            }
            List<String> list = new ArrayList<>();
//
//            String name = (String) authentication.getPrincipal().getAttributes().get("name");
           Account userAccount = this.userRepository.findByUsername(username);
            String role = userAccount.getRole();
//
//            list.add("ROLE_USER");
            list.add(role);
            String jwtToken = jwtAuthenticationService.createToken(username, list);
//            headers.add("JwtAuthorization","Bearer " + jwtToken );
//            headers.add(HttpHeaders.AUTHORIZATION, "Bearer" + jwtToken);

            Cookie cookie = new Cookie("Jwt", "Bearer" + jwtToken);
            servletResponse.addCookie(cookie);


            HttpEntity<String> entity = new HttpEntity<String>("", headers);

            ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);



            Map userAttributes = response.getBody();
            model.addAttribute("name", userAttributes.get("name"));
        }




//        response.setHeader("username", credentials.getUsername());



        return "loginSuccess";
    }
}