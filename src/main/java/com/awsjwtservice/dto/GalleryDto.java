package com.awsjwtservice.dto;

import com.awsjwtservice.domain.Gallery;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GalleryDto {
    private Long id;
    private String title;
    private String filePath;
    private Long userSeq;

    public Gallery toEntity(){
        Gallery build = Gallery.builder()
                .id(id)
                .title(title)
                .filePath(filePath)
                .userSeq(userSeq)
                .build();
        return build;
    }

    @Builder
    public GalleryDto(Long id, String title, String filePath, Long userSeq) {
        this.id = id;
        this.title = title;
        this.filePath = filePath;
        this.userSeq = userSeq;
    }
}