package com.awsjwtservice.dto;

import com.awsjwtservice.domain.Account;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;

@Getter
public class SessionUserDto implements Serializable {
    private String username;
    private String email;
    private String picture;

    public SessionUserDto(Account user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
