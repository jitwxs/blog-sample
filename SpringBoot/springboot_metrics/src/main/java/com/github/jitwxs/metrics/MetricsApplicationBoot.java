package com.github.jitwxs.metrics;

import com.github.jitwxs.metrics.enums.MetricsEnum;
import com.github.jitwxs.metrics.support.CounterMetricsUtil;
import com.github.jitwxs.metrics.support.GaugeMetricsUtil;
import com.github.jitwxs.metrics.support.TimerMetricsUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author jitwxs
 * @date 2020年11月15日 20:22
 */
@Component
public class MetricsApplicationBoot implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 注册指标
        for (MetricsEnum metricsEnum : MetricsEnum.values()) {
            switch (metricsEnum.getType()) {
                case COUNTER:
                    CounterMetricsUtil.register(metricsEnum);
                    break;
                case GAUGE:
                    GaugeMetricsUtil.register(metricsEnum);
                    break;
                case TIMER:
                    TimerMetricsUtil.register(metricsEnum);
                    break;
                default:
                    break;
            }
        }
    }
}
