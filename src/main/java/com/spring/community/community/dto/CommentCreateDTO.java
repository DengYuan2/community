package com.spring.community.community.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private long parentId;
    private String content;
    private Integer type;
}