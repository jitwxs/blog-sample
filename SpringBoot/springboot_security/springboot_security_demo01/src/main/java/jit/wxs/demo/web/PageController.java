package jit.wxs.demo.web;

import jit.wxs.demo.security.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class PageController {
    /**
     * 跳转到登陆成功页
     */
    @RequestMapping(SecurityConstants.LOGIN_SUCCESS_URL)
    public String showSuccessPage() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("登录成功，当前登陆用户：" + name);

        return "home.html";
    }

    /**
     * 跳转到登录页
     */
    @RequestMapping(SecurityConstants.UN_AUTHENTICATION_URL)
    public String showAuthenticationPage() {
        return "login.html";
    }

    /**
     * 处理Spring Security登录异常信息
     */
    @RequestMapping(SecurityConstants.LOGIN_FAILURE_URL)
    public void loginError(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        AuthenticationException exception =
                (AuthenticationException)request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        try {
            response.getWriter().write(exception.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}