package com.awsjwtservice.dto;

import com.awsjwtservice.domain.Account;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;


@Getter
@Builder
public class  SessionUserDto implements Serializable {
    private String username;
    private String email;
    private String picture;
    private Long userSeq;
    private String role;


}
