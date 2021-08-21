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
    private String picture;

    @Column(nullable = true)
    private String role;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String test;

    //@NotNull
    @CreationTimestamp
    private LocalDateTime regdate;

    @UpdateTimestamp
    private LocalDateTime uptdate;

    @ColumnDefault("0")
    private int delCheck;

    // enum의 name을 DB에 저장하기 위해, default는 숫자
    @Enumerated(EnumType.STRING)
    private LoginProvider loginProvider;

    @Embedded
    private Address address;

    //Unable to evaluate the expression Method threw 'org.hibernate.LazyInitializationException' exception. -> change LAZY to EAGER
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @Column(nullable = true)
    private List<Orders> orders;


    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @Column(nullable = true)
    private List<Round> rounds;




    @Builder
    public Account(String username, String email, String picture, String role, String password, LoginProvider loginProvider) { //using the private field variables
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.password = password;
        this.loginProvider = loginProvider;
    }

    public Account update(String username, String picture, LoginProvider loginProvider) {
        this.username = username;
        this.picture = picture;
        this.loginProvider = loginProvider;
        return this;
    }

    // .save, getter, setter, .etc methods provided by lombok
}

