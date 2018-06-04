package jit.wxs.demo.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝支付设置
 * @author jitwxs
 * @since 2018/6/4 19:04
 */
@Configuration
public class AliPayConfig {
    @Value("${alipay.gateway_url}")
    private String GATEWAY_URL;
    @Value("${alipay.app_id}")
    private String APP_ID;
    @Value("${alipay.merchant_private_key}")
    private String MERCHANT_PRIVATE_KEY;
    @Value("${alipay.alipay_public_key}")
    private String ALIPAY_PUBLIC_KEY;
    @Value("${alipay.sign_type}")
    private String SIGN_TYPE;

    @Bean
    AlipayClient alipayClient() {
        return new DefaultAlipayClient
                (GATEWAY_URL, APP_ID, MERCHANT_PRIVATE_KEY, "json", "utf-8", ALIPAY_PUBLIC_KEY, SIGN_TYPE);
    }
}
