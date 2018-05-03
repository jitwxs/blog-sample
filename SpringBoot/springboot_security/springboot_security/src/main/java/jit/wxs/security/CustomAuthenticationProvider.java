package jit.wxs.security;

import jit.wxs.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jitwxs
 * @date 2018/3/29 19:49
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取用户输入的用户名和密码
        String inputName = authentication.getName();
        String inputPassword = authentication.getCredentials().toString();

        CustomWebAuthenticationDetails details = (CustomWebAuthenticationDetails) authentication.getDetails();
        String verifyCode = details.getVerifyCode();
        if(!validateVerify(verifyCode)) {
            throw new DisabledException("验证码输入错误");
        }

        // userDetails一定非空，否则会抛异常
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(inputName);

        // 对密码进行验证，如有需要可以进行自定义加密解密的判断
        if (!PasswordUtils.validatePassword(inputPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("密码错误，登录失败！");
        }

        return new UsernamePasswordAuthenticationToken(inputName, inputPassword, userDetails.getAuthorities());
    }

    private boolean validateVerify(String inputVerify) {
        //获取当前线程绑定的request对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 不分区大小写
        // 这个validateCode是在servlet中存入session的名字
        String validateCode = ((String) request.getSession().getAttribute("validateCode")).toLowerCase();
        inputVerify = inputVerify.toLowerCase();

        //System.out.println("验证码：" + validateCode + "用户输入：" + inputVerify);
        return validateCode.equals(inputVerify);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
