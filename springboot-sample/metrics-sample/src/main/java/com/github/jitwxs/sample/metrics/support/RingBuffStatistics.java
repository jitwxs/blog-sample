package com.github.jitwxs.sample.metrics.support;

import com.github.jitwxs.sample.common.TimeUtils;
import com.github.jitwxs.sample.metrics.enums.IMetricsEnum;
import com.github.jitwxs.sample.metrics.enums.IMetricsTagEnum;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ringBuffer 监控，支持：积压、消费耗时
 * @author jitwxs
 * @date 2020-11-12 10:18 上午
 */
public class RingBuffStatistics {
    private final Map<String, EventTypeBO> map = new HashMap<>();

    private static final String DEFAULT_EVENT_TYPE = "default";

    @Data
    @Builder
    static class EventTypeBO {
        private String name;

        private AtomicLong total;

        private AtomicLong consumed;

        private IMetricsTagEnum consumedMetrics;
    }

    /**
     * @param backLog 积压指标
     * @param consumeTime 耗时指标
     *
     * @param index 分片ID
     */
    public RingBuffStatistics(final IMetricsEnum backLog, final IMetricsEnum consumeTime, final int index) {
        this(backLog, consumeTime, null, index, Collections.singletonList(DEFAULT_EVENT_TYPE));
    }

    /**
     * @param backLog 积压指标
     * @param consumeTime 耗时指标
     *
     * @param index 分片ID
     * @param eventTypeList 事件类型列表，用于区分 ringBuffer 中事件
     */
    public RingBuffStatistics(final IMetricsEnum backLog,
                              final IMetricsEnum consumeTime,
                              final IMetricsEnum qps,
                              final int index,
                              final List<String> eventTypeList) {
        for (String name : eventTypeList) {
            String[] tags = new String[]{"shard", String.valueOf(index), IMetricsTagEnum.FUNCTION, name};

            final IMetricsTagEnum consumeMetricsTagEnum = consumeTime.createVirtualMetricsTagEnum(tags);

            final EventTypeBO eventType = EventTypeBO.builder()
                    .name(name)
                    .total(new AtomicLong())
                    .consumed(new AtomicLong())
                    .consumedMetrics(consumeMetricsTagEnum)
                    .build();

            map.putIfAbsent(name, eventType);

            // 手动注册
            if (qps != null) {
                MetricsUtil.registerGauge(qps.createVirtualMetricsTagEnum(tags), () -> qps(name));
            }
            MetricsUtil.registerGauge(backLog.createVirtualMetricsTagEnum(tags), () -> backlogSize(name));
            MetricsUtil.simpleRegister(consumeMetricsTagEnum);
        }
    }

    public void incr() {
        this.incr(DEFAULT_EVENT_TYPE);
    }

    public void incr(String eventType) {
        final EventTypeBO eventTypeBO = map.get(eventType);
        if(eventTypeBO != null) {
            eventTypeBO.total.incrementAndGet();
        }
    }

    public void desc(long startTime) {
        this.desc(startTime, DEFAULT_EVENT_TYPE);
    }

    public void desc(long startTime, String eventType) {
        final EventTypeBO eventTypeBO = map.get(eventType);
        if(eventTypeBO != null) {
            eventTypeBO.consumed.incrementAndGet();
            MetricsUtil.recordTimerOrGauge(eventTypeBO.getConsumedMetrics(), TimeUtils.diffMs(startTime));
        }
    }

    public long backlogSize(String eventType) {
        final EventTypeBO eventTypeBO = map.get(eventType);
        if(eventTypeBO != null) {
            return eventTypeBO.getTotal().get() - eventTypeBO.getConsumed().get();
        } else {
            return 0;
        }
    }

    public long qps(String eventType) {
        final EventTypeBO eventTypeBO = map.get(eventType);
        if(eventTypeBO != null) {
            return eventTypeBO.getTotal().get();
        } else {
            return 0;
        }
    }
}
