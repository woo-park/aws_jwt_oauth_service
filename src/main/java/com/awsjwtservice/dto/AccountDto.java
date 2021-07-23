package com.awsjwtservice.dto;


import com.awsjwtservice.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto implements Serializable {
    private String username;
    private String email;
    private String password;
    private String role;

    @Embedded
    private Address address;

}

