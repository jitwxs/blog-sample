package jit.wxs.demo.security;

/**
 * Security 相关常量
 * @author jitwxs
 * @since 2019/1/8 23:50
 */
public class SecurityConstants {
    /**
     * 当没有权限时，被引导跳转的 Url
     */
    public static final String UN_AUTHENTICATION_URL = "/sso";
    /**
     * 登陆成功后，被引导跳转的 Url
     */
    public static final String LOGIN_SUCCESS_URL = "/";
    /**
     * 登陆失败，被引导跳转的 Url
     */
    public static final String LOGIN_FAILURE_URL = "/login/error";

    /**
     * 用户名密码登录请求处理url
     */
    public static final String LOGIN_PROCESSING_URL_FORM = "/authentication/form";
    /**
     * 手机验证码登录请求处理url
     */
    public static final String LOGIN_PROCESSING_URL_MOBILE = "/authentication/mobile";

    /**
     * 手机验证码登录手机号表单字段名
     */
    public static final String LOGIN_MOBILE_PARAMETER = "mobile";
    /**
     * 手机验证码登录验证码表单字段名
     */
    public static final String LOGIN_MOBILE_CODE_PARAMETER = "smsCode";
    /**
     * 验证码登陆表单字段名
     */
    public static final String VALIDATE_CODE_PARAMETER = "verifyCode";

    /**
     * 验证码相关 Url 前缀
     * 包括图形验证码图片、短信验证码接口等等...
     */
    public static final String VALIDATE_CODE_URL_PREFIX = "/code";
}
