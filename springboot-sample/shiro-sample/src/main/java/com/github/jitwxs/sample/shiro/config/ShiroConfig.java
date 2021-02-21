package com.github.jitwxs.sample.shiro.config;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jitwxs
 * @date 2018/3/20 10:00
 */
@Configuration
public class ShiroConfig {
    /**
     * 注入ShiroRealm
     * 不能省略，可能导致service无法注入
     */
    @Bean
    public ShiroRealm shiroRealm() {
        return new ShiroRealm();
    }

    /**
     * 注入securityManager
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(shiroRealm());
        return manager;
    }

    /**
     * Filter工厂，设置过滤条件与跳转条件
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();

        // Shiro的核心安全接口
        bean.setSecurityManager(securityManager);

        // 设置登陆页
        bean.setLoginUrl("/login");

        // 自定义拦截规则
        Map<String,String> map = new HashMap<>(16);
        map.put("/", "anon");
        // 设置退出登陆
        map.put("/logout", "logout");
        // 对所有用户认证
        map.put("/**", "authc");

        bean.setFilterChainDefinitionMap(map);
        return bean;
    }

    /**
     * 注册AuthorizationAttributeSourceAdvisor
     * 如果要开启注解，必须添加
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);

        return advisor;
    }
}
