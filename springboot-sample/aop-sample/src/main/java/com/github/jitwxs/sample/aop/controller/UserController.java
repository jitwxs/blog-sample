package com.github.jitwxs.sample.aop.controller;

import com.github.jitwxs.sample.aop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/delete")
    public String restDelete(String name) {
        userService.delete(name);
        return "success";
    }

    @GetMapping("/exception")
    public void restException() {
        int i = 1 / 0;
    }
}