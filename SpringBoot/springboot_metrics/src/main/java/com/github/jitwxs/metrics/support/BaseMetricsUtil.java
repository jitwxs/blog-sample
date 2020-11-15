package com.github.jitwxs.metrics.support;

import com.github.jitwxs.metrics.enums.MetricsEnum;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.util.Assert;

/**
 * @author jitwxs
 * @date 2020年11月15日 19:42
 */
public class BaseMetricsUtil {
    public static MeterRegistry meterRegistry;

    protected static String CLASS_NAME = new Object() {
        public String getClassName() {
            String clazzName = this.getClass().getName();
            return clazzName.substring(0, clazzName.lastIndexOf("$"));
        }
    }.getClassName();

    public static void basicCheck(final MetricsEnum metricsEnum) {
        Assert.notNull(meterRegistry, CLASS_NAME + " meterRegistry not allow null");

        String[] tags = metricsEnum.getTags();
        if(tags != null && tags.length % 2 != 0) {
            throw new IllegalArgumentException(CLASS_NAME + "metrics tags must appear in pairs");
        }
    }
}
