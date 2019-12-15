package com.spring.community.community.controller;

import com.spring.community.community.dto.CommentCreateDTO;
import com.spring.community.community.dto.ResultDTO;
import com.spring.community.community.exception.CustomizeErrorCode;
import com.spring.community.community.model.Comment;
import com.spring.community.community.model.User;
import com.spring.community.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,//如此，页面传过来的json数据可以自动转为CommentDTO类型
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //if (commentCreateDTO == null || commentCreateDTO.getContent() == null || commentCreateDTO.getContent()=="") {
        //引入了commons-lang3依赖后可以直接下面这么写。isBlank方法其实就是封装了的上面的两个判断
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {//前端也要有这样的判断
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setLikeCount(0L);
        comment.setCommentator(user.getId());
        commentService.insert(comment);
        return ResultDTO.okOf();
    }
}
