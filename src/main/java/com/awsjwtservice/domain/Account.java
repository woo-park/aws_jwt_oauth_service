package com.awsjwtservice.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name="USER")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String picture;     //not sure how picture is a string

    @Column(nullable = true)
    private String role;

    @Column(nullable = true)
    private String password;


    @Builder
    public Account(String username, String email, String picture, String role, String password) { //using the private field variables
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.password = password;
    }                       // now you have .save and .etc methods provided by lombok


    public Account update(String username, String picture) {
        this.username = username;
        this.picture = picture;

        return this;
    }

}

