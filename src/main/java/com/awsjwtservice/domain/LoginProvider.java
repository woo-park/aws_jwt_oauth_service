package com.awsjwtservice.domain;

public enum LoginProvider {
    FORM("form"),
    KAKAO("kakaos"),
    NAVER("naver"),
    GOOGLE("google");

    private String value;

    LoginProvider(String value) {
        this.value = value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
