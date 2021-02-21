package com.github.jitwxs.sample.metrics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jitwxs
 * @date 2021-02-17 2:30 下午
 */
@Getter
@AllArgsConstructor
public enum MetricsTagEnum implements IMetricsTagEnum {

    READ_COUNT_1(MetricsEnum.READ_COUNT, new String[]{"video_name", "法外狂徒张三"}),
    READ_COUNT_2(MetricsEnum.READ_COUNT, new String[]{"video_name", "不讲武德年轻人"}),

    SYSTEM_VISITOR_SIZE(MetricsEnum.VISITOR_SIZE, new String[]{"type", "system"}),

    USERINFO_REQUEST_TIME(MetricsEnum.REQUEST_TIME, new String[]{"url", "/userInfo"}),
    ;

    private final IMetricsEnum metricsEnum;

    private final String[] tags;
}
