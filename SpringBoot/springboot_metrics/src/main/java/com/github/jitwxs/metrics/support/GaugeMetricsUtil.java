package com.github.jitwxs.metrics.support;

import com.github.jitwxs.metrics.enums.MetricsEnum;
import io.micrometer.core.instrument.Gauge;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * For Gauge Metrics
 * @author jitwxs
 * @date 2020年11月15日 20:09
 */
public class GaugeMetricsUtil extends BaseMetricsUtil {
    private static final Map<MetricsEnum, Double> REGISTER_MAP = new HashMap<>();

    public static void register(MetricsEnum metricsEnum) {
        basicCheck(metricsEnum);
        Assert.isTrue(!REGISTER_MAP.containsKey(metricsEnum), "this metrics already register");

        Gauge.builder(metricsEnum.getName(), REGISTER_MAP, e -> e.get(metricsEnum))
                .tags(metricsEnum.getTags())
                .description(metricsEnum.getDescription())
                .register(meterRegistry);

        REGISTER_MAP.put(metricsEnum, 0D);
    }

    public static void gauge(MetricsEnum metricsEnum, double value) {
        REGISTER_MAP.put(metricsEnum, value);
    }
}
