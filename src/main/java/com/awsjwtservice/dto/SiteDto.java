package com.awsjwtservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;


@Getter
@Builder
public class SiteDto {
    private String title;
    private String siteUrl;
    private String userSeq;


}

