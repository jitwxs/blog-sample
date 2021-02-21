package com.github.jitwxs.sample.i18n.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

@Controller
public class TestController {
    @Autowired
    private MessageSource messageSource;

    @GetMapping("/test")
    @ResponseBody
    public Object test(Locale locale) {
        String[] params = {"Jack Zhang"};
        return messageSource.getMessage("welcome.msg", params, locale);
    }

    @GetMapping({"/login","/"})
    public String showLoginPage(){
        return "login.html";
    }
}