package com.spring.community.community.service;

import com.spring.community.community.dto.CommentCreateDTO;
import com.spring.community.community.dto.CommentDTO;
import com.spring.community.community.enums.CommentTypeEnum;
import com.spring.community.community.exception.CustomizeErrorCode;
import com.spring.community.community.exception.CustomizeException;
import com.spring.community.community.mapper.CommentMapper;
import com.spring.community.community.mapper.QuestionExtMapper;
import com.spring.community.community.mapper.QuestionMapper;
import com.spring.community.community.mapper.UserMapper;
import com.spring.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

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

    public List<CommentDTO> listByQuestionId(Long id) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        commentExample.setOrderByClause("gmt_create desc");//按时间倒叙排列
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size()==0) return new ArrayList<>();
        //获取去重的评论人的id
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());//java8语法，拿到评论者们的id
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人，并转换为map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //转换comment为commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
