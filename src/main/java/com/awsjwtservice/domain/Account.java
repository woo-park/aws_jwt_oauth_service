package com.awsjwtservice.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account implements Serializable {
    @Id
    @Column(name = "account_id")
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

    @Column(nullable = true)
    private String test;

    //@NotNull
    @CreationTimestamp
    private LocalDateTime regdate;

    //@NotNull
    @UpdateTimestamp
    private LocalDateTime uptdate;

    @ColumnDefault("0") //default 0
    private int delCheck;

    @Enumerated(EnumType.STRING) // enum의 name을 DB에 저장하기 위해, 없을 경우 enum의 숫자가 들어간다.
    private LoginProvider loginProvider;

    /* order fields testing */

    @Embedded
    private Address address;



    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)        //Unable to evaluate the expression Method threw 'org.hibernate.LazyInitializationException' exception. -> change LAZY to EAGER
    @Column(nullable = true)
    private List<Orders> orders;


    @Builder
    public Account(String username, String email, String picture, String role, String password, LoginProvider loginProvider) { //using the private field variables
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.password = password;
        this.loginProvider = loginProvider;
    }                       // now you have .save and .etc methods provided by lombok

    public Account update(String username, String picture, LoginProvider loginProvider) {
        this.username = username;
        this.picture = picture;
        this.loginProvider = loginProvider;
        return this;
    }

}

