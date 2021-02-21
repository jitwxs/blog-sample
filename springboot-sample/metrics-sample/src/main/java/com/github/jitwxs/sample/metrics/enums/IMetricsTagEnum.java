package com.github.jitwxs.sample.metrics.enums;

/**
 * @author jitwxs
 * @date 2020-11-06 12:49 下午
 */
public interface IMetricsTagEnum {
    String FUNCTION = "function";

    IMetricsEnum getMetricsEnum();

    String[] getTags();
}