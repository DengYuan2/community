package com.spring.community.community.service;

import com.spring.community.community.enums.CommentTypeEnum;
import com.spring.community.community.exception.CustomizeErrorCode;
import com.spring.community.community.exception.CustomizeException;
import com.spring.community.community.mapper.CommentMapper;
import com.spring.community.community.mapper.QuestionExtMapper;
import com.spring.community.community.mapper.QuestionMapper;
import com.spring.community.community.model.Comment;
import com.spring.community.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId()==null || comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType()==CommentTypeEnum.COMMENT.getType()){//回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else{//回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }else {
                commentMapper.insert(comment);//希望45行和47行一起成功或失败，故需要作事务处理,在方法上加@Transactional注解
                question.setCommentCount(1);
                questionExtMapper.incCommentCount(question);//希望45行和47行一起成功或失败，故需要作事务处理，在方法上加@Transactional注解
            }
        }
    }
}
