package com.spring.community.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {
    @GetMapping("/hello")
    public String helllo(@RequestParam(name = "fu")String name, Model model){
        model.addAttribute("fu",name);
        return "hello";

    }
}
