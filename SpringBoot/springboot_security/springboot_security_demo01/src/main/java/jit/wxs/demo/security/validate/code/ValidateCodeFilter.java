package jit.wxs.demo.security.validate.code;

import jit.wxs.demo.security.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码登陆过滤器
 * @author jitwxs
 * @since 2019/1/9 0:01
 */
@Slf4j
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isProtectedUrl(request)) {
            String verifyCode = request.getParameter(SecurityConstants.VALIDATE_CODE_PARAMETER);
            if(!validateVerify(verifyCode)) {
                //手动设置异常
                request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION",new DisabledException("验证码输入错误"));
                // 转发到错误Url
                request.getRequestDispatcher(SecurityConstants.VALIDATE_CODE_ERR_URL).forward(request,response);
            } else {
                filterChain.doFilter(request,response);
            }
        } else {
            filterChain.doFilter(request,response);
        }
    }

    private boolean validateVerify(String inputVerify) {
        //获取当前线程绑定的request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 不分区大小写
        // 这个validateCode是在servlet中存入session的名字
        String validateCode = ((String) request.getSession().getAttribute("validateCode")).toLowerCase();
        inputVerify = inputVerify.toLowerCase();

        log.info("验证码：{}, 用户输入：{}", validateCode, inputVerify);
        return validateCode.equals(inputVerify);
    }

    /**
     * 拦截登陆请求
     */
    private boolean isProtectedUrl(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) &&
                pathMatcher.match(SecurityConstants.LOGIN_PROCESSING_URL_FORM, request.getServletPath());
    }
}
