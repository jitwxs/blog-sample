package com.github.jitwxs.sample.metrics.support;

import com.github.jitwxs.sample.common.ThreadPoolUtil;
import com.github.jitwxs.sample.common.TimeUtils;
import com.github.jitwxs.sample.metrics.enums.IMetricsEnum;
import com.github.jitwxs.sample.metrics.enums.IMetricsTagEnum;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Timer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Metrics 注册、采集 工具类
 * <p>
 * 一、指标类型
 * <p>
 * 本工具类支持以下三种类型的指标：<p>Gauge、Counter、Timer</p>
 * <p>
 * 二、指标注册
 * <p>
 * 支持以下三种方式的指标注册
 * <p>
 * （1）对于 {@link IMetricsTagEnum} 中的全部指标支持<strong>自动注册</strong>，仅需要在启动类中调用 {@link #init(Class)} 方法即可
 * <p>
 * （2）对于不能明确定义 IMetricsTagEnum 的指标，例如需要分片的，在定义 {@link IMetricsEnum} 后，调用 {@link IMetricsEnum#createVirtualMetricsTagEnum}
 * 创建虚拟的 {@link IMetricsTagEnum}，然后调用 {@link #simpleRegister(IMetricsTagEnum...)} 进行<strong>手动注册</strong>
 * <p>
 * （3）对于统计 RingBuffer backLog（积压），请使用 {@link #registerGauge(IMetricsTagEnum, Supplier)} 初始化 backLog 指标，
 * 需要结合 {@link RingBuffStatistics} 类使用
 * <p>
 * 三、指标采集
 * <p>
 * （1）对于 Gauge、Timer 类型指标，请调用 {@link #recordTimerOrGauge(IMetricsTagEnum, long)} 进行指标采集
 * <p>
 * （2）对于 Counter 类型指标，请调用 {@link #recordCounter(IMetricsTagEnum)} 或 {@link #recordCounter(IMetricsTagEnum, double)} 进行指标采集
 *
 * @author jitwxs
 * @date 2020-11-06 7:11 下午
 */
@Slf4j
public class MetricsUtil extends BaseMetricsUtil {
    private static final Map<IMetricsTagEnum, MetricsWrapper> METRICS_MAP = new HashMap<>();

    static {
        ThreadPoolUtil.newScheduledExecutor(1, "metrics-manager-thread-pool").scheduleWithFixedDelay(() -> METRICS_MAP.values().stream()
                .filter(e -> e.getType() != IMetricsEnum.Type.COUNTER).filter(e -> TimeUtils.diffMs(e.getLastTime()) > 10_000)
                .forEach(e -> e.recordTimerOrGauge(0L)), 15, 15, TimeUnit.SECONDS);
    }

    /**
     * For IMetricsTagEnum
     */
    public static <T extends IMetricsTagEnum> void init(Class<T> clazz) {
        try {
            Method method = clazz.getDeclaredMethod("values");
            simpleRegister((T[]) method.invoke(null));
        } catch (final Exception e) {
            log.error("metrics gauge error,class={}", clazz.getSimpleName(), e);
        }
    }

    /**
     * For RingBuffer
     */
    public static void registerGauge(final IMetricsTagEnum metricsTagEnum,
                                     final Supplier<Number> supplier) {
        if (!basicCheck(metricsTagEnum)) {
            return;
        }

        Gauge.builder(metricsTagEnum.getMetricsEnum().getName(), supplier)
                .tags(metricsTagEnum.getTags())
                .description(metricsTagEnum.getMetricsEnum().getDesc())
                .register(meterRegistry);
    }

    /**
     * For Common
     */
    public static void simpleRegister(IMetricsTagEnum... metricsTagEnums) {
        if (metricsTagEnums != null && metricsTagEnums.length > 0) {
            for (IMetricsTagEnum metricsTagEnum : metricsTagEnums) {
                if (!basicCheck(metricsTagEnum)) {
                    continue;
                }

                final IMetricsEnum.Type type = metricsTagEnum.getMetricsEnum().getType();

                if (type == IMetricsEnum.Type.GAUGE) {
                    Gauge.builder(metricsTagEnum.getMetricsEnum().getName(), METRICS_MAP, m -> (long) m.get(metricsTagEnum).getMetrics())
                            .tags(metricsTagEnum.getTags())
                            .description(metricsTagEnum.getMetricsEnum().getDesc())
                            .register(meterRegistry);

                    METRICS_MAP.put(metricsTagEnum, MetricsWrapper.newInstance(type, 0L));
                } else if (type == IMetricsEnum.Type.COUNTER) {
                    final Counter cnt = Counter.builder(metricsTagEnum.getMetricsEnum().getName())
                            .tags(metricsTagEnum.getTags())
                            .description(metricsTagEnum.getMetricsEnum().getDesc())
                            .register(meterRegistry);

                    METRICS_MAP.put(metricsTagEnum, MetricsWrapper.newInstance(type, cnt));
                } else if (type == IMetricsEnum.Type.TIMER) {
                    final Timer timer = Timer.builder(metricsTagEnum.getMetricsEnum().getName())
                            .tags(metricsTagEnum.getTags())
                            .description(metricsTagEnum.getMetricsEnum().getDesc())
                            .publishPercentiles(0.5, 0.9, 0.95, 0.99)
                            .register(meterRegistry);

                    METRICS_MAP.put(metricsTagEnum, MetricsWrapper.newInstance(type, timer));
                }
            }
        }
    }

    /**
     * For Counter
     */
    public static void recordCounter(IMetricsTagEnum metricsTagEnum) {
        recordCounter(metricsTagEnum, 1.0D);
    }

    /**
     * For Counter
     */
    public static void recordCounter(IMetricsTagEnum metricsTagEnum, double size) {
        if (METRICS_MAP.containsKey(metricsTagEnum)) {
            METRICS_MAP.get(metricsTagEnum).recordCounter(size);
        }
    }

    /**
     * For Timer、Gauge
     */
    public static void recordTimerOrGauge(final IMetricsTagEnum metricsTagEnum, final long value) {
        if (METRICS_MAP.containsKey(metricsTagEnum)) {
            METRICS_MAP.get(metricsTagEnum).recordTimerOrGauge(value);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MetricsWrapper {
        private IMetricsEnum.Type type;

        private Object metrics;

        private long lastTime = TimeUtils.nowMs();

        public static MetricsWrapper newInstance(IMetricsEnum.Type type, Object metrics) {
            return new MetricsWrapper(type, metrics, TimeUtils.nowMs());
        }

        private void recordCounter(final double value) {
            ((Counter) this.metrics).increment(value);
            this.lastTime = TimeUtils.nowMs();
        }

        public void recordTimerOrGauge(final long value) {
            if (this.type == IMetricsEnum.Type.TIMER) {
                ((Timer) this.metrics).record(value, TimeUnit.MILLISECONDS);
            } else if (this.type == IMetricsEnum.Type.GAUGE) {
                this.metrics = value;
            }
            this.lastTime = TimeUtils.nowMs();
        }
    }
}