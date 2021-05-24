package com.awsjwtservice.config.security;

import com.awsjwtservice.domain.Account;
import com.awsjwtservice.repository.UserRepository;
import com.awsjwtservice.dto.SessionUserDto;
import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Autowired
    private final UserRepository userRepository;
//    private final HttpRequest httpRequest;
    private final HttpSession httpSession;


    private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
    private RestOperations restOperations;
    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {};


//    public OAuth2UserServiceImpl() {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
//        this.restOperations = restTemplate;
//    }

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

        RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);


        ResponseEntity<Map<String, Object>> response;
        try {
            RestTemplate restTemplate = new RestTemplate();
            assert request != null;
            response = restTemplate.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
            System.out.println(response);
        } catch (OAuth2AuthorizationException ex) {

        }


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


        httpSession.setAttribute("user", SessionUserDto.builder().role(user.getRole()).username(user.getUsername()).email(user.getEmail()).picture(user.getPicture()).userSeq(user.getId()).build());
        httpSession.setAttribute("httpSessionTestAttribute", "OAuth2UserServiceImpl");
        // end?
        return new DefaultOAuth2User(
                Collections.singleton(
                new SimpleGrantedAuthority(user.getRole())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );

        // loadUser(OAuth2UserRequest userRequest)은 OAuth2LoginAuthenticationProvider line 116에서 invoked됩니다.
        // loadUser는 OAuth2LoginAuthenticationProvider implements AuthenticationProvider에서
        // authenticate() method안에서 invoked됩니다.


        // OAuth2LoginAuthenticationProvider가 끝이난후에는, ProviderManager로 갑니다
        // 이때 ProviderManager은 provider을 OAuth2LoginAuthenticationProvider로 지정해 두었습니다.
        // result = provider.authenticate(authentication);
        // result 가 OAuth2LoginAuthenticationToken 입니다. 안에 principal, accessToken, refreshtoken, authorities, authenticated, etc. 등 존재합니다.

        // 끝날쯤에 eventPublisher.publishAuthenticationSuccess(result);
        // return result;

        // 하고나면, OAuth2LoginAuthenticationFilter로 이동합니다.
        // OAuth2LoginAuthenticationFilter에 연동 되 있는 this.getAuthenticationManager() .authenticate(authenticationRequest);
        // 이 메소드는 OAuth2LoginAuthenticationToken를 return한다
        // OAuth2AuthenticationToken oauth2Authentication,
        // OAuth2AuthorizedClient authorizedClient
        // 이렇게 둘을 만들고,
        // this.authorizedClientRepository.saveAuthorizedClient(authorizedClient, oauth2Authentication, request, response);
        // return oauth2Authentication;
        // authorizedClientRepository에 저장한후, OAuth2AuthenticationToken을 반환한다.

        // sidenote로
        // clientRegistrationRepository 에는       authorizedClientService 안에 clientRegistrationRepository 안에 registrations 안에 ["kakao", "google", "naver"] 가 정의되어 준비되있다.

        // this.authorizedClientRepository.saveAuthorizedClient(...)
        // method가 정상적으로 invoked됬다면,       clientRegistrationRepository 안에 authorizedClientService 안에 authorizedClients.size()가 1로 증가하면서,
        // OAuth2AuthorizedClient가 추가된다.

//        OAuth2LoginAuthenticationFilter가 extends AbstractAuthenticationProcessingFilter 했기때문에,
//                return oauth2Authentication한 후로는 AbstractAuthenticationProcessingFilter의
        // doFilter로 돌아간다

        // filterChainProxy

        // OncePerRequestFilter

        //.....쭉 타다가

        // http11Processor 쯤 되니, SESSION cookie가 browser에 입혀지고, browser page도 보이기 시작한다.
        // 근데 의문점은 왜 response.req.serverCookies[0] = "Cookie SESSION=Yzk.......;0 null null"
        // 왜 response의 req안에 있는거지? response의 res 아니고

    }



    private Account saveOrUpdate(OAuthAttributes attributes) {
        Account account = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getUsername(), attributes.getPicture(), attributes.getLoginProvider()))
                .orElse(attributes.toEntity()); // if findByEmail fails

//        Account user = userRepository.findByEmail(attributes.getEmail())
//                .map(entity -> entity.update(attributes.getUsername(), attributes.getPicture()))    //update takes in two args
//                .orElse(attributes.toEntity()); // if findByEmail fails

        // User user Entity made
        return userRepository.save(account);   // now saved to repo
    }

}
