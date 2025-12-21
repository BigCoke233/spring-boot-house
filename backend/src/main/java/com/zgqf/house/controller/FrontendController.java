package com.zgqf.house.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }

    @RequestMapping(value = "/**/{path:[^\\.]*}")
    public String redirectNested() {
        return "forward:/index.html";
    }
}
