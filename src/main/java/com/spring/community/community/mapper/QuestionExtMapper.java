package com.spring.community.community.mapper;

import com.spring.community.community.model.Comment;
import com.spring.community.community.model.Question;
import com.spring.community.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import sun.util.resources.cldr.id.LocaleNames_id;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);
    int incCommentCount(Question record);
    List<Question> selectRelated(Question question);
}