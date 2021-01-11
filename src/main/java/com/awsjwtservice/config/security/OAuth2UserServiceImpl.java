package com.awsjwtservice.config.security;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
//    private final HttpRequest httpRequest;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delagate = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delagate.loadUser(userRequest);         // userRequest from argument

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();    // to figure out/ differentiate from naver, or google login

        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();    // naver vs google

        OAuthAttributes attributes = OAuthAttributes.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes()); //   OAuth2UserService 를 통해 가져온 data 를 담는 class입니다

        Account user = saveOrUpdate(attributes);   // saveOrUpdate method needs to be defined here

//        httpSession.setAttribute("user", new SessionUser(user));    //  session 에 사용자 정보를 저장하기 위한 dto class

//        DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
//        bearerTokenResolver.setBearerTokenHeaderName(HttpHeaders.PROXY_AUTHORIZATION);


        return new DefaultOAuth2User(
                Collections.singleton(
//                        new SimpleGrantedAuthority(user.getRoleKey())), // userRole 이 class일적에
                new SimpleGrantedAuthority(user.getRole())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }



    private Account saveOrUpdate(OAuthAttributes attributes) {
        Account user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getUsername(), attributes.getPicture()))    //update takes in two args
                .orElse(attributes.toEntity()); // if findByEmail fails

        // User user Entity made
        return userRepository.save(user);   // now saved to repo
    }

}
