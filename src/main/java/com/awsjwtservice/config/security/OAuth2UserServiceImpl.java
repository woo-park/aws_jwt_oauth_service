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


        /*
            userRequest.accessToken.tokenType = OAuth2AccessToken
            userRequest.accessToken.scopes[0] = name
            userRequest.accessToken.scopes[1] = email
            userRequest.accessToken.scopes[2] = profile_image
            userRequest.accessToken.tokenValue = "...."
        */


        // OAuthAttributes에 들어가는 outh id type => "naver", "google", "kakao"
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();    // to figure out/ differentiate from naver, or google login

        // 유저 이름           i.e. userNameAttributeName = "response"
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();    // naver vs google

        // OAuth2UserService 를 통해 가져온 data 를 담는 class입니다. dto라고 생각하면 됩니다.
        OAuthAttributes attributes = OAuthAttributes.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());


        // 정의한 saveOrUpdate()을 통해 Repository에 save||update. 한후에 persist된 Account를 반환합니다.
        Account user = saveOrUpdate(attributes);

//        httpSession.setAttribute("user", new SessionUser(user));    //  session 에 사용자 정보를 저장하기 위한 dto class

//        DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
//        bearerTokenResolver.setBearerTokenHeaderName(HttpHeaders.PROXY_AUTHORIZATION);



        // end?
        return new DefaultOAuth2User(
                Collections.singleton(
//                        new SimpleGrantedAuthority(user.getRoleKey())), // userRole 이 class일적에
                new SimpleGrantedAuthority(user.getRole())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );

        // loadUser(OAuth2UserRequest userRequest)은 OAuth2LoginAuthenticationProvider line 116에서 invoked됩니다.
        // loadUser는 OAuth2LoginAuthenticationProvider implements AuthenticationProvider에서
        // authenticate() method안에서 invoked됩니다.
        
    }



    private Account saveOrUpdate(OAuthAttributes attributes) {
        Account user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getUsername(), attributes.getPicture()))    //update takes in two args
                .orElse(attributes.toEntity()); // if findByEmail fails

        // User user Entity made
        return userRepository.save(user);   // now saved to repo
    }

}
