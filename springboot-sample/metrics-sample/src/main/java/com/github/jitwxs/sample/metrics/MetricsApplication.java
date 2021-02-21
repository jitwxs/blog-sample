package com.github.jitwxs.sample.metrics;

import com.github.jitwxs.sample.metrics.enums.MetricsTagEnum;
import com.github.jitwxs.sample.metrics.support.MetricsUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MetricsApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(MetricsApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 注册指标
        MetricsUtil.init(MetricsTagEnum.class);
    }
}