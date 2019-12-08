package com.spring.community.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;//avatarUrl也可，fastjson可以自动进行驼峰和下划线的转换哦，不过此时set和get方法的名称就改变啦
}
