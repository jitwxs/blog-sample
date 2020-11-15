package com.github.jitwxs.metrics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.github.jitwxs.metrics.enums.MetricsTypeEnum.*;

/**
 * @author jitwxs
 * @date 2020年11月15日 20:03
 */
@Getter
@AllArgsConstructor
public enum MetricsEnum {
    DEFAULT("default", null, "default description", UNKNOWN),

    READ_COUNT_1("read_count", new String[]{"video_name", "法外狂徒张三"}, "阅读量统计", COUNTER),
    READ_COUNT_2("read_count", new String[]{"video_name", "不讲武德年轻人"}, "阅读量统计", COUNTER),

    VISITOR_SIZE("visitor_size", null, "系统访问量", GAUGE),

    PORTAL_REQUEST_TIME("portal_request_time", new String[]{"url", "/userInfo"}, "前端请求耗时", TIMER),
    ;

    private final String name;

    private final String[] tags;

    private final String description;

    private final MetricsTypeEnum type;
}
