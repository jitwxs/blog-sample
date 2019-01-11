package jit.wxs.demo.security;

import jit.wxs.demo.security.authentication.*;
import jit.wxs.demo.security.validate.code.ValidateCodeSecurityConfig;
import jit.wxs.demo.security.validate.mobile.SmsCodeAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Spring Security 核心配置类
 * @author jitwxs
 * @since 2019/1/8 23:28
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DefaultAuthenticationFailureHandler failureHandler;
    @Autowired
    private DefaultAuthenticationSuccessHandler successHandler;
    @Autowired
    private DefaultLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private DefaultUserDetailsService userDetailService;
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired
    private DataSource dataSource;

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * token 持久化
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 如果token表不存在，使用下面语句可以初始化该表；若存在，会报错。
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    /**
     * 将 DefaultPermissionEvaluator 配置进 DefaultWebSecurityExpressionHandler 中
     */
    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler(){
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new DefaultPermissionEvaluator());
        return handler;
    }

    /**
     * 配置密码加密方式，这里选择不加密
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return charSequence.toString();
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return s.equals(charSequence.toString());
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 添加关于验证码登录的配置
                .apply(validateCodeSecurityConfig).and()
                .apply(smsCodeAuthenticationSecurityConfig).and()
                // 设置登陆页
                .formLogin()
                    // 没有权限时跳转的Url
                    .loginPage(SecurityConstants.UN_AUTHENTICATION_URL)
                    // 默认登陆Url
                    .loginProcessingUrl(SecurityConstants.LOGIN_PROCESSING_URL_FORM)
                    // 设置登陆成功/失败处理逻辑
                    .successHandler(successHandler)
                    .failureHandler(failureHandler)
                    .permitAll().and()
                .logout()
                    .logoutUrl(SecurityConstants.LOGOUT_URL)
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .deleteCookies("JSESSIONID").and()
                .sessionManagement()
                    .invalidSessionUrl(SecurityConstants.INVALID_SESSION_URL)
                    // 单用户最大session数
                    .maximumSessions(1)
                    // 当达到maximumSessions时，是否保留已经登录的用户
                    .maxSessionsPreventsLogin(false)
                    // 当达到maximumSessions时，旧用户被踢出后的操作
                    .expiredSessionStrategy(new DefaultExpiredSessionStrategy())
                    .sessionRegistry(sessionRegistry()).and().and()
                .rememberMe()
                    .tokenRepository(persistentTokenRepository())
                    // 有效时间：单位s
                    .tokenValiditySeconds(60)
                    .userDetailsService(userDetailService).and()
                .authorizeRequests()
                    // 如果有允许匿名的url，填在下面
                    .antMatchers(SecurityConstants.VALIDATE_CODE_URL_PREFIX + "/*").permitAll()
                    .anyRequest()
                    .authenticated().and()
                // 关闭CSRF跨域
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/assets/**");
    }
}
