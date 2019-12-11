package com.spring.community.community.controller;

import com.spring.community.community.dto.PageDTO;
import com.spring.community.community.mapper.UserMapper;
import com.spring.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String Index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "5")Integer size){
        //以下关于cookies的每次在每个controller都要重写，故Interception拦截器处理
        //在interceptor包处理
//        Cookie[] cookies = request.getCookies();
//        if(cookies!=null&&cookies.length!=0)
//        for (Cookie cookie:cookies){//cookie以键值对保存，key用getName获取，value用getValue获取
//            if (cookie.getName().equals("token")){
//                String token = cookie.getValue();
//                User user = userMapper.findByToken(token);
//                if (user!=null){
//                    request.getSession().setAttribute("user",user);
//                }
//                break;
//            }
//        }

        //interceptor包下的SessionInterceptor已经把原来的cookie放到session里啦
        PageDTO pageNation = questionService.list(page,size);
        model.addAttribute("pageNation",pageNation);
        return "index";
    }
}
