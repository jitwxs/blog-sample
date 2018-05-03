package jit.wxs.security;

import jit.wxs.entity.SysRole;
import jit.wxs.entity.SysUser;
import jit.wxs.entity.SysUserRole;
import jit.wxs.service.SysRoleService;
import jit.wxs.service.SysUserRoleService;
import jit.wxs.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jitwxs
 * @date 2018/3/29 19:49
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取用户输入的用户名和密码
        String inputName = authentication.getName();
        String inputPassword = authentication.getCredentials().toString();

        // 判断用户是否存在
        SysUser user = userService.selectByName(inputName);
        if (user == null) {
            throw new UsernameNotFoundException("未找到指定用户");
        }

        // 对密码进行验证，如有需要可以进行自定义加密解密的判断
        if (!inputPassword.equals(user.getPassword())) {
            throw new BadCredentialsException("密码错误，登录失败！");
        }

        // 添加权限
        List<SysUserRole> userRoles = userRoleService.listByUserId(user.getId());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (SysUserRole userRole : userRoles) {
            SysRole role = roleService.selectById(userRole.getRoleId());
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return new UsernamePasswordAuthenticationToken(inputName, inputPassword, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
