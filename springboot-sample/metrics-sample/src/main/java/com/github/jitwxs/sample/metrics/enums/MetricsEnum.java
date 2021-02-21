package com.github.jitwxs.sample.metrics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jitwxs
 * @date 2020年11月15日 20:03
 */
@Getter
@AllArgsConstructor
public enum MetricsEnum implements IMetricsEnum {
    READ_COUNT("read_count", Type.COUNTER, "阅读量统计"),
    VISITOR_SIZE("visitor_size", Type.GAUGE, "访问量统计"),
    REQUEST_TIME("request_time",Type.TIMER, "请求耗时");

    private final String name;
    private final Type type;
    private final String desc;
}