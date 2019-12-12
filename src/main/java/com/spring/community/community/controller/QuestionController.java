package com.spring.community.community.controller;

import com.spring.community.community.dto.QuestionDTO;
import com.spring.community.community.mapper.QuestionMapper;
import com.spring.community.community.model.User;
import com.spring.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Integer id,
                           Model model, HttpServletRequest request){

//        User user = (User)request.getSession().getAttribute("user");
//        if (user==null) return "redirect:/";

        QuestionDTO questionDTO = questionService.getById(id);
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
