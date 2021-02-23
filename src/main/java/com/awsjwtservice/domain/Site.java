package com.awsjwtservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private long userSeq;
//    private List<String> keywords = new ArrayList<>();
    private String siteUrl;

    @ColumnDefault("0")
    private int delCheck;

    @CreationTimestamp
    private LocalDateTime regdate;

    @UpdateTimestamp
    private LocalDateTime uptdate;

    @Builder
    public Site(String siteUrl, String title, Long userSeq) {
        this.siteUrl = siteUrl;
        this.title = title;
        this.userSeq = userSeq;
    }

    @Override
    public String toString() {
        return String.format("id=%s, title=%s, userSeq=%s, siteUrl=%s, delCheck=%s, regdate=%s, uptdate=%s", this.id, this.title, this.userSeq, this.siteUrl, this.delCheck, this.regdate, this.uptdate);
    }
}
