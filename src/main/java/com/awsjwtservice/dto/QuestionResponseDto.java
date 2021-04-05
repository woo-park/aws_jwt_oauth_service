package com.awsjwtservice.dto;


import com.awsjwtservice.domain.Question;
import lombok.Getter;


@Getter
public class QuestionResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    public QuestionResponseDto(Question entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}