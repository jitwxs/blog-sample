package com.github.jitwxs.sample.oauth;

import com.github.jitwxs.sample.oauth.utils.HttpClientUtils;
import com.github.jitwxs.sample.oauth.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author jitwxs
 * @since 2018/5/21 14:28
 */
@Controller
public class LoginController {
    @Autowired
    private OauthService oauthService;

    private static String GITHUB_CLIENT_ID = "0307dc634e4c5523cef2";
    private static String GITHUB_CLIENT_SECRET = "707647176eb3bef1d4c2a50fcabf73e0401cc877";
    private static String GITHUB_REDIRECT_URL = "http://127.0.0.1:8080/githubCallback";

    private static String QQ_APP_ID = "101933357";
    private static String QQ_APP_KEY = "900292039ed5408d941dad762ab68383";
    private static String QQ_REDIRECT_URL = "http://127.0.0.1:8080/qqCallback";

    @RequestMapping("/githubLogin")
    public void githubLogin(HttpServletResponse response) throws Exception {
        // Github认证服务器地址
        String url = "https://github.com/login/oauth/authorize";
        // 生成并保存state，忽略该参数有可能导致CSRF攻击
        String state = oauthService.genState();
        // 传递参数response_type、client_id、state、redirect_uri
        String param = "response_type=code&" + "client_id=" + GITHUB_CLIENT_ID + "&state=" + state
                + "&redirect_uri=" + GITHUB_REDIRECT_URL;

        // 1、请求Github认证服务器
        response.sendRedirect(url + "?" + param);
    }

    /**
     * GitHub回调方法
     * @param code 授权码
     * @param state 应与发送时一致
     * @author jitwxs
     * @since 2018/5/21 15:24
     */
    @RequestMapping("/githubCallback")
    public void githubCallback(String code, String state, HttpServletResponse response) throws Exception {
        // 验证state，如果不一致，可能被CSRF攻击
        if(!oauthService.checkState(state)) {
            throw new Exception("State验证失败");
        }

        // 2、向GitHub认证服务器申请令牌
        String url = "https://github.com/login/oauth/access_token";
        // 传递参数grant_type、code、redirect_uri、client_id
        String param = "grant_type=authorization_code&code=" + code + "&redirect_uri=" +
                GITHUB_REDIRECT_URL + "&client_id=" + GITHUB_CLIENT_ID + "&client_secret=" + GITHUB_CLIENT_SECRET;

        // 申请令牌，注意此处为post请求
        String result = HttpClientUtils.sendPostRequest(url, param);

        /*
         * result示例：
         * 失败：error=incorrect_client_credentials&error_description=The+client_id+and%2For+client_secret+passed+are+incorrect.&
         * error_uri=https%3A%2F%2Fdeveloper.github.com%2Fapps%2Fmanaging-oauth-apps%2Ftroubleshooting-oauth-app-access-token-request-errors%2F%23incorrect-client-credentials
         * 成功：access_token=7c76186067e20d6309654c2bcc1545e41bac9c61&scope=&token_type=bearer
         */
        Map<String, String> resultMap = HttpClientUtils.params2Map(result);
        // 如果返回的map中包含error，表示失败，错误原因存储在error_description
        if(resultMap.containsKey("error")) {
            throw  new Exception(resultMap.get("error_description"));
        }

        // 如果返回结果中包含access_token，表示成功
        if(!resultMap.containsKey("access_token")) {
            throw  new Exception("获取token失败");
        }

        // 得到token和token_type
        String accessToken = resultMap.get("access_token");
        String tokenType = resultMap.get("token_type");

        // 3、向资源服务器请求用户信息，携带access_token和tokenType
        String userUrl = "https://api.github.com/user";
        String userParam = "access_token=" + accessToken + "&token_type=" + tokenType;
        
        // 申请资源
        String userResult = HttpClientUtils.sendGetRequest(userUrl, userParam);

        // 4、输出用户信息
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(userResult);
    }

    @RequestMapping("/qqLogin")
    public void qqLogin(HttpServletResponse response) throws Exception {
        // QQ认证服务器地址
        String url = "https://graph.qq.com/oauth2.0/authorize";
        // 生成并保存state，忽略该参数有可能导致CSRF攻击
        String state = oauthService.genState();
        // 传递参数response_type、client_id、state、redirect_uri
        String param = "response_type=code&" + "client_id=" + QQ_APP_ID + "&state=" + state
                + "&redirect_uri=" + QQ_REDIRECT_URL;

        // 1、请求QQ认证服务器
        response.sendRedirect(url + "?" + param);
    }

    /**
     * QQ回调方法
     * @param code 授权码
     * @param state 应与发送时一致
     * @author jitwxs
     * @since 2018/5/21 15:24
     */
    @RequestMapping("/qqCallback")
    public void qqCallback(String code, String state, HttpServletResponse response) throws Exception {
        // 验证state，如果不一致，可能被CSRF攻击
        if(!oauthService.checkState(state)) {
            throw new Exception("State验证失败");
        }

        // 2、向QQ认证服务器申请令牌
        String url = "https://graph.qq.com/oauth2.0/token";
        // 传递参数grant_type、code、redirect_uri、client_id
        String param = "grant_type=authorization_code&code=" + code + "&redirect_uri=" +
                QQ_REDIRECT_URL + "&client_id=" + QQ_APP_ID + "&client_secret=" + QQ_APP_KEY;

        // 申请令牌，注意此处为post请求
        // QQ获取到的access token具有3个月有效期，用户再次登录时自动刷新。
        String result = HttpClientUtils.sendPostRequest(url, param);

        /*
         * result示例：
         * 成功：access_token=A24B37194E89A0DDF8DDFA7EF8D3E4F8&expires_in=7776000&refresh_token=BD36DADB0FE7B910B4C8BBE1A41F6783
         */
        Map<String, String> resultMap = HttpClientUtils.params2Map(result);
        // 如果返回结果中包含access_token，表示成功
        if(!resultMap.containsKey("access_token")) {
            throw  new Exception("获取token失败");
        }
        // 得到token
        String accessToken = resultMap.get("access_token");

        // 3、使用Access Token来获取用户的OpenID
        String meUrl = "https://graph.qq.com/oauth2.0/me";
        String meParams = "access_token=" + accessToken;
        String meResult = HttpClientUtils.sendGetRequest(meUrl, meParams);
        // 成功返回如下：callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
        // 取出openid
        String openid = getQQOpenid(meResult);

        // 4、使用Access Token以及OpenID来访问和修改用户数据
        String userInfoUrl = "https://graph.qq.com/user/get_user_info";
        String userInfoParam = "access_token=" + accessToken + "&oauth_consumer_key=" + QQ_APP_ID + "&openid=" + openid;
        String userInfo = HttpClientUtils.sendGetRequest(userInfoUrl, userInfoParam);

        // 5、输出用户信息
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(userInfo);
    }

    /**
     * 提取Openid
     * @param str 形如：callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
     * @author jitwxs
     * @since 2018/5/22 21:37
     */
    private String getQQOpenid(String str) {
        // 获取花括号内串
        String json = str.substring(str.indexOf("{"), str.indexOf("}") + 1);
        // 转为Map
        Map<String, String> map = JsonUtils.jsonToPojo(json, Map.class);
        return map.get("openid");
    }
}
