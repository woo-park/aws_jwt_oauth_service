package com.awsjwtservice.controller;

import java.security.Principal;
import java.util.*;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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



    @GetMapping("/oauth_login")
    public String getLoginPage(Principal principal, Model model, HttpServletRequest request) {

        Enumeration headers = request.getHeaderNames();



        while(headers.hasMoreElements()) {

            String headerName = (String)headers.nextElement();

            String value = request.getHeader(headerName);

            System.out.println("headerName:"+headerName+","+value);

        }


        String referrer = request.getHeader("Referer");
        System.out.println(referrer);
        request.getSession().setAttribute("prevPage", referrer);

//        System.out.println("Authorization Header Value ::" + request.getHeader("Authorization") + request.getHeader("JwtAuthorization"));
        return "oauthLogin";
    }

    @GetMapping("/loginSuccess")                    //authentication =- OAuth2AuthenticationToken
    public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication, HttpServletResponse servletResponse) {


        // 문제가 authentication.getName() 이 이름이 아닌 고유번호를 return 한다.
        // String username = authentication.getName();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName()); //client = OAuth2AuthorizedClient

        ClientRegistrationRepository clientRegistrationRepository;

        // 이둘은 같다
        Object userDetailsTest = (Object)authentication.getPrincipal();
        Object currentAuth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // currentAuth = DefaultOauth2User@12140
        // userDetailsTest = DefaultOauth2User@12140

        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();

        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            // 헤더에 Authorization : Bearer + token 을 넣어줍니다.
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());


            // 유저 이름 추출
            String username = null;
            if (authentication.getAuthorizedClientRegistrationId().equals("naver")) {
                username = (String) authentication.getPrincipal().getAttributes().get("name");
            } else if (authentication.getAuthorizedClientRegistrationId().equals("kakao") ) {
                // 카카오는 name 이 아닌 nickname 으로 들고와야합니다.
                Map<String, Object> details = (Map<String, Object>) authentication.getPrincipal().getAttributes();

                // 1. details 를 HashMap 으로 변환하는 방법중 하나 입니다.
                // final Map<?, ?> modifiable = new HashMap<>(details);

                // 2. type casting 을 통해 두번 HashMap 으로 변환후, nickname 을 찾아옵니다.
                username = (String)((HashMap)((HashMap)details.get("kakao_account")).get("profile")).get("nickname");
            } else if (authentication.getAuthorizedClientRegistrationId().equals("google") ) {
                username = (String) authentication.getPrincipal().getAttributes().get("name");
            }



            HttpEntity<String> entity = new HttpEntity<String>("", headers);
            ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();


            model.addAttribute("name", userAttributes.get("name"));
        }

        return "redirect:mypage";
    }

    @GetMapping("/denied")
    public String accessDenied(@RequestParam(value = "exception", required = false) String exception, Principal principal, Model model) throws Exception {

        Account account = null;

        if (principal instanceof UsernamePasswordAuthenticationToken) {
            account = (Account) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }

        if (principal instanceof OAuth2AuthenticationToken) {

        }


        if(account != null) {
            model.addAttribute("exception", exception);
        }

        return "denied";
    }

}