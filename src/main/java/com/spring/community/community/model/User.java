package com.spring.community.community.model;

import lombok.Data;

@Data//因为引入了lombok哦
public class User {
    private int id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
