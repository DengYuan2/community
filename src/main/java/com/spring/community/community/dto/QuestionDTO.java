package com.spring.community.community.dto;

import com.spring.community.community.model.User;
import lombok.Data;

/**
 * 想要展示一列问题，需要在question数据库查出所有需要的数据，但是
 * 用户的头像不在question表中，而在user表中，
 * 故用QuestionDTO将需要的数据连接起来【注：question类和user类各
 * 对应一个实体，不宜轻易增添不在数据库中的数据】
 */
@Data
public class QuestionDTO {
    private Long id;
    private Long creator;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
