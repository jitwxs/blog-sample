package com.github.jitwxs.sample.metrics.support;

import com.github.jitwxs.sample.metrics.enums.IMetricsTagEnum;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jitwxs
 * @date 2020年11月15日 19:42
 */
@Slf4j
public class BaseMetricsUtil {
    public static MeterRegistry meterRegistry;

    protected static String CLASS_NAME = new Object() {
        public String getClassName() {
            String clazzName = this.getClass().getName();
            return clazzName.substring(0, clazzName.lastIndexOf("$"));
        }
    }.getClassName();

    public static boolean basicCheck(final IMetricsTagEnum metricsTagEnum) {
        if (meterRegistry == null) {
            log.warn("metrics registry is null,class={}", CLASS_NAME);
            return false;
        }

        final String[] tags = metricsTagEnum.getTags();

        if (tags != null && tags.length % 2 != 0) {
            log.error("metrics count error,class={},tags={}", CLASS_NAME, tags);
            return false;
        }

        return true;
    }
}
