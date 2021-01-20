package com.awsjwtservice.config.security;


import com.awsjwtservice.domain.Account;
import com.awsjwtservice.domain.LoginProvider;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String username;
    private String email;
    private String picture;
    private LoginProvider loginProvider;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String username,
                           String email,
                           String picture
    ) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.username = username;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes)
    {
        if("naver".equals(registrationId)) { //incoming param
            return ofNaver("id", attributes);
        }

        if("kakao".equals(registrationId)){

//            loginProvider = LoginProvider.KAKAO;
            return ofKakao("id", attributes);
        }


        return ofGoogle(userNameAttributeName, attributes);             //currying
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes ) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .username((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profileImage"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .username((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)        //huh
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String,Object> response = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");
        return OAuthAttributes.builder()
                .username((String)profile.get("nickname"))
                .email((String)response.get("email"))
                .picture((String)profile.get("profile_image_url"))

                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }




    public Account toEntity() {
        return Account.builder()
                .username(username)
                .email(email)
                .picture(picture)
                .role("ROLE_USER")
//                .loginProvider(LoginProvider.KAKAO)

                .build();
    }

}
