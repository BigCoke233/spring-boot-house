package com.zgqf.house.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect(HttpServletRequest request) {
        if (request.getRequestURI().startsWith("/api")) {
            return "forward:/error";
        }
        return "forward:/index.html";
    }

    @RequestMapping(value = "/**/{path:[^\\.]*}")
    public String redirectNested(HttpServletRequest request) {
        if (request.getRequestURI().startsWith("/api")) {
            return "forward:/error";
        }
        return "forward:/index.html";
    }
}
