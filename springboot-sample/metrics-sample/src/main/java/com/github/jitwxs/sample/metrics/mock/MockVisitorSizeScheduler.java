package com.github.jitwxs.sample.metrics.mock;

import com.github.jitwxs.sample.metrics.enums.MetricsTagEnum;
import com.github.jitwxs.sample.metrics.support.MetricsUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author jitwxs
 * @date 2020年11月15日 20:19
 */
@Service
public class MockVisitorSizeScheduler {
    @Scheduled(initialDelay = 100, fixedDelay = 1500)
    public void mockVisitorSize() {
        int visitorSize = new Random().nextInt(100);
        MetricsUtil.recordTimerOrGauge(MetricsTagEnum.SYSTEM_VISITOR_SIZE, visitorSize);
    }
}