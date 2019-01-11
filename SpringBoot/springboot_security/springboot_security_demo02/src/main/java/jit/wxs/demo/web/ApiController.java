package jit.wxs.demo.web;

import jit.wxs.demo.security.SecurityConstants;
import jit.wxs.demo.util.ResultMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ApiController {
    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/role/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String printAdmin() {
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";
    }

    @GetMapping("/role/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String printUser() {
        return "如果你看见这句话，说明你有ROLE_USER角色";
    }

    @GetMapping("/permission/admin/r")
    @PreAuthorize("hasPermission('/admin','r')")
    public String printAdminR() {
        return "如果你看见这句话，说明你访问/admin路径具有r权限";
    }

    @GetMapping("/permission/admin/c")
    @PreAuthorize("hasPermission('/admin','c')")
    public String printAdminC() {
        return "如果你看见这句话，说明你访问/admin路径具有c权限";
    }

    @GetMapping(SecurityConstants.VALIDATE_CODE_URL_PREFIX + "/sms")
    public String sms(String mobile, HttpSession session) {
        int code = (int) Math.ceil(Math.random() * 9000 + 1000);

        Map<String, Object> map = new HashMap<>(16);
        map.put("mobile", mobile);
        map.put("code", code);

        session.setAttribute("smsCode", map);

        log.info("{}：为 {} 设置短信验证码：{}", session.getId(), mobile, code);

        return "发送成功";
    }

    /**
     * 读取当前用户鉴权信息
     */
    @GetMapping("/me")
    public Object me(Authentication authentication) {
        return authentication;
    }

    /**
     * 踢出指定用户
     * todo: 还需要清理持久化表，不然无法踢出自动登陆用户，我就不做了
     */
    @PostMapping("/kick")
    public ResultMap removeUserSessionByUsername(String username) {
        int count = 0;

        // 获取session中所有的用户信息
        List<Object> users = sessionRegistry.getAllPrincipals();
        for (Object principal : users) {
            if (principal instanceof User) {
                String principalName = ((User) principal).getUsername();
                if (principalName.equals(username)) {
                    /*
                     * 获取指定用户所有的 session 信息
                     * 参数二：是否包含过期的Session
                     */
                    List<SessionInformation> sessionsInfo = sessionRegistry.getAllSessions(principal, false);
                    if (null != sessionsInfo && sessionsInfo.size() > 0) {
                        for (SessionInformation sessionInformation : sessionsInfo) {
                            sessionInformation.expireNow();
                            count++;
                        }
                    }
                }
            }
        }

        return new ResultMap(getClass() + ":removeUserSessionByUsername()", "操作成功，清理session共" + count + "个");
    }

    /**
     * 处理 session 过期
     */
    @RequestMapping(SecurityConstants.INVALID_SESSION_URL)
    public ResultMap invalid() {
        return new ResultMap(getClass().getName() + ":invalid()", "Session 已过期，请重新登录");
    }

    /**
     * 处理验证码错误
     */
    @RequestMapping(SecurityConstants.VALIDATE_CODE_ERR_URL)
    public ResultMap codeError() {
        return new ResultMap(getClass().getName() + ":codeError()", "验证码输入错误");
    }
}
