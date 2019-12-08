package com.spring.community.community.controller;

import com.spring.community.community.dto.PageDTO;
import com.spring.community.community.dto.QuestionDTO;
import com.spring.community.community.mapper.QuestionMapper;
import com.spring.community.community.mapper.UserMapper;
import com.spring.community.community.model.Question;
import com.spring.community.community.model.User;
import com.spring.community.community.service.QuestionService;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String Index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "5")Integer size){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0)
        for (Cookie cookie:cookies){//cookie以键值对保存，key用getName获取，value用getValue获取
            if (cookie.getName().equals("token")){
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                if (user!=null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        PageDTO pageNation = questionService.list(page,size);
        model.addAttribute("pageNation",pageNation);
        return "index";
    }
}
