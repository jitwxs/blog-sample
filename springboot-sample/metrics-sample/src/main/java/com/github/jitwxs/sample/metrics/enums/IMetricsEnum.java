package com.github.jitwxs.sample.metrics.enums;

/**
 * Metrics 监控指标枚举
 * @author jitwxs
 * @date 2020-11-06 11:29 上午
 */
public interface IMetricsEnum {
    enum Type {GAUGE, COUNTER, TIMER}

    String getName();

    Type getType();

    String getDesc();

    default IMetricsTagEnum createVirtualMetricsTagEnum(String[] tags) {
        IMetricsEnum iMetricsEnum = this;
        return new IMetricsTagEnum() {
            @Override
            public IMetricsEnum getMetricsEnum() {
                return iMetricsEnum;
            }

            @Override
            public String[] getTags() {
                return tags;
            }
        };
    }
}