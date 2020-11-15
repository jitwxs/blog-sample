package com.github.jitwxs.metrics.support;

import com.github.jitwxs.metrics.enums.MetricsEnum;
import io.micrometer.core.instrument.Counter;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * For Counter Metrics
 * @author jitwxs
 * @date 2020年11月15日 19:47
 */
public class CounterMetricsUtil extends BaseMetricsUtil {
    private static final Map<MetricsEnum, Counter> REGISTER_MAP = new HashMap<>();

    public static void register(MetricsEnum metricsEnum) {
        basicCheck(metricsEnum);
        Assert.isTrue(!REGISTER_MAP.containsKey(metricsEnum), "this metrics already register");

        Counter counter = Counter.builder(metricsEnum.getName())
                .tags(metricsEnum.getTags())
                .description(metricsEnum.getDescription())
                .register(meterRegistry);

        REGISTER_MAP.put(metricsEnum, counter);
    }

    public static void increment(MetricsEnum metricsEnum) {
        increment(metricsEnum, 1.0);
    }

    public static void increment(MetricsEnum metricsEnum, double value) {
        Counter counter = REGISTER_MAP.get(metricsEnum);
        if(counter != null) {
            counter.increment(value);
        }
    }
}
