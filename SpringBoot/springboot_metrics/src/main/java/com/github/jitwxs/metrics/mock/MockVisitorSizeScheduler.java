package com.github.jitwxs.metrics.mock;

import com.github.jitwxs.metrics.enums.MetricsEnum;
import com.github.jitwxs.metrics.support.GaugeMetricsUtil;
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

        GaugeMetricsUtil.gauge(MetricsEnum.VISITOR_SIZE, visitorSize);
    }
}