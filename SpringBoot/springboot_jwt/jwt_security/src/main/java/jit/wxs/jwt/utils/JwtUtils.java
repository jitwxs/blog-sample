package jit.wxs.jwt.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * @author jitwxs
 * @since 2018/5/2 18:49
 */
public class JwtUtils {
    // 签名密钥（高度保密）
    private static final String SECRET = "qYYjXt7s1C*nEC%9RCwQGFA$YwPr$Jrj";
    // 签名算法
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
    // token前缀
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 生成JWT token
     * @param map 传入数据
     * @param maxAge 有效期（单位：ms）
     * @author jitwxs
     * @since 2018/5/3 23:19
     */
    public static String sign(Map<String,Object> map, long maxAge) {
        return sign(map, null, null, maxAge);
    }

    /**
     * 生成JWT token
     * @param map 传入数据
     * @param issuer 签发者
     * @param subject 面向用户
     * @param maxAge 有效期（单位：ms）
     * @author jitwxs
     * @since 2018/5/3 23:19
     */
    public static String sign(Map<String,Object> map, String issuer, String subject, long maxAge) {
        Date now  = new Date(System.currentTimeMillis());
        String jwt = Jwts.builder()
                .setClaims(map) // 设置自定义数据
                .setIssuedAt(now) // 设置签发时间
                .setExpiration(new Date(now.getTime() + maxAge)) // 设置过期时间
                .setIssuer(issuer) // 设置签发者
                .setSubject(subject) // 设置面向用户
                .signWith(signatureAlgorithm, SECRET)
                .compact();
        return TOKEN_PREFIX + jwt;
    }

    /**
     * 验证JWT token并返回数据。当验证失败时，抛出异常
     * @author jitwxs
     * @since 2018/5/3 23:20
     */
    public static Map unSign(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX,""))
                    .getBody();
        }catch (Exception e){
            throw new IllegalStateException("Token验证失败："+e.getMessage());
        }
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("userName","admin");
        map.put("userId","001");

        String token = JwtUtils.sign(map, 3600_000);
//        String token = JwtUtils.sign(map, "jitwxs","普通用户",3600_000);
        System.out.println(JwtUtils.unSign(token));
    }
}

