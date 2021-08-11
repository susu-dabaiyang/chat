package com.zyh.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@CrossOrigin
@Controller
public class Index {
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String findAll() {
        return "/user";
    }

}
