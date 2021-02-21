package com.github.jitwxs.sample.alipay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝支付设置
 * @author jitwxs
 * @since 2018/6/4 19:04
 */
@Configuration
@Getter
public class AliPayConfig {
    @Value("${alipay.gateway_url}")
    private String gatewayUrl;
    @Value("${alipay.app_id}")
    private String appId;
    @Value("${alipay.merchant_private_key}")
    private String merchantPrivateKey;
    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;
    @Value("${alipay.sign_type}")
    private String signType;
    @Value("${alipay.uid}")
    private String sellerId;
    @Value("${alipay.notify_url}")
    private String notifyUrl;
    @Value("${alipay.return_url}")
    private String returnUrl;

    @Bean
    AlipayClient alipayClient() {
        return new DefaultAlipayClient
                (gatewayUrl, appId, merchantPrivateKey, "json", "utf-8", alipayPublicKey, signType);
    }
}
