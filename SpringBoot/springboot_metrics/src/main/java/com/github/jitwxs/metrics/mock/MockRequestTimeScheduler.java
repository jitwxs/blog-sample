package com.github.jitwxs.metrics.mock;

import com.github.jitwxs.metrics.enums.MetricsEnum;
import com.github.jitwxs.metrics.support.TimerMetricsUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author jitwxs
 * @date 2020年11月15日 20:19
 */
@Service
public class MockRequestTimeScheduler {
    @Scheduled(initialDelay = 100, fixedDelay = 1500)
    public void mockVisitorSize() {
        TimerMetricsUtil.record(MetricsEnum.PORTAL_REQUEST_TIME, new Random().nextInt(1000), TimeUnit.MILLISECONDS);
    }
}