package com.spring.community.community.mapper;

import com.spring.community.community.model.Comment;
import com.spring.community.community.model.Question;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}