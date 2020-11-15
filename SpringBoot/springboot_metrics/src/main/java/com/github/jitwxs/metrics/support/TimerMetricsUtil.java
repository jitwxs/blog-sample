package com.github.jitwxs.metrics.support;

import java.util.concurrent.TimeUnit;

import com.github.jitwxs.metrics.enums.MetricsEnum;
import io.micrometer.core.instrument.Timer;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * For Time Metrics
 * @author jitwxs
 * @date 2020年11月15日 20:13
 */
public class TimerMetricsUtil extends BaseMetricsUtil {
    private static final Map<MetricsEnum, Timer> REGISTER_MAP = new HashMap<>();

    public static void register(MetricsEnum metricsEnum) {
        basicCheck(metricsEnum);
        Assert.isTrue(!REGISTER_MAP.containsKey(metricsEnum), "this metrics already register");

        Timer timer = Timer.builder(metricsEnum.getName())
                .tags(metricsEnum.getTags())
                .description(metricsEnum.getDescription())
                .publishPercentiles(0.5, 0.8, 0.95, 0.99) // 指定百分位数
                .register(meterRegistry);

        REGISTER_MAP.put(metricsEnum, timer);
    }

    public static void record(MetricsEnum metricsEnum, long time, TimeUnit unit) {
        Timer timer = REGISTER_MAP.get(metricsEnum);
        if(timer != null) {
            timer.record(time, unit);
        }
    }
}
