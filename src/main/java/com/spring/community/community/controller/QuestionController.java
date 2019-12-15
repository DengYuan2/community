package com.spring.community.community.controller;

import com.spring.community.community.dto.CommentDTO;
import com.spring.community.community.dto.QuestionDTO;
import com.spring.community.community.service.CommentService;
import com.spring.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Long id,Model model){
//        User user = (User)request.getSession().getAttribute("user");
//        if (user==null) return "redirect:/";
        QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> comments= commentService.listByQuestionId(id);
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        return "question";
    }
}
