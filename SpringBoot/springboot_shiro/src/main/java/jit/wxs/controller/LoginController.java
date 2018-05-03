package jit.wxs.controller;

import jit.wxs.entity.User;
import jit.wxs.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jitwxs
 * @date 2018/3/20 11:22
 */
@Controller
public class LoginController {
    @Autowired
    IUserService userService;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @PostMapping("/login")
    public String login(User user) {

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                user.getName(), user.getPassword());
        //进行验证，这里可以捕获异常，然后返回对应信息
        subject.login(usernamePasswordToken);

        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "home.html";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "redirect:/logout";
    }

    @RequiresRoles("admin")
    @RequiresPermissions("create")
    @RequestMapping("/adminCreate")
    @ResponseBody
    public String adminCreate() {
        return "只有[admin]身份且具有[create]权限才能看见这句话";
    }
}