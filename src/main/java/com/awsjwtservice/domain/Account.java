package com.awsjwtservice.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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



    @Builder
    public Account(String username, String email, String picture, String role, String password ) { //using the private field variables
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.password = password;
//        this.loginProvider = loginProvider;
    }                       // now you have .save and .etc methods provided by lombok


    public Account update(String username, String picture) {
        this.username = username;
        this.picture = picture;

        return this;
    }

}

