package com.spring.community.community.model;

import lombok.Data;
import org.apache.ibatis.annotations.Insert;
import org.omg.PortableInterceptor.INACTIVE;
@Data
public class Question {
    private Integer id;
    private Integer creator;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
}
