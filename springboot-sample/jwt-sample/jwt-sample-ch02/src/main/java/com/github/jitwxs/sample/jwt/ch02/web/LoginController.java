package com.github.jitwxs.sample.jwt.ch02.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jitwxs
 * @date 2018/3/30 1:30
 */
@Controller
public class LoginController {
    private final String USER_ID_KEY = "USER_ID";

    @RequestMapping("/login/error")
    @ResponseBody
    public String loginError(HttpServletRequest request, HttpServletResponse response) {
        AuthenticationException exception =
                (AuthenticationException)request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        return exception.toString();
    }

    @GetMapping("/api/admin")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object helloAdmin(@RequestAttribute(value = USER_ID_KEY)  Integer userId) {
        return "Welcome Admin! Your USER_ID : " + userId;
    }

    @GetMapping("/api/user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public Object helloUser(@RequestAttribute(value = USER_ID_KEY)  Integer userId) {
        return "Welcome User! Your USER_ID : " + userId;
    }
}