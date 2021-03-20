package com.awsjwtservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
//@Table(name = "gallery")
public class Gallery {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String filePath;

    @CreationTimestamp
    private LocalDateTime regdate;

    @UpdateTimestamp
    private LocalDateTime uptdate;

    private Long userSeq;

    @ColumnDefault("0")
    private int delCheck;

    @Builder
    public Gallery(Long id, String title, String filePath, Long userSeq) {
        this.id = id;
        this.title = title;
        this.filePath = filePath;
        this.userSeq = userSeq;
    }

    public void setDelCheck(int delCheck) {
        this.delCheck = delCheck;
    }
}
