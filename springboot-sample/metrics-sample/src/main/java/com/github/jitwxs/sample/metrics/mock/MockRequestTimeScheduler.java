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
public class MockRequestTimeScheduler {
    @Scheduled(initialDelay = 100, fixedDelay = 1500)
    public void mockVisitorSize() {
        MetricsUtil.recordTimerOrGauge(MetricsTagEnum.USERINFO_REQUEST_TIME, new Random().nextInt(1000));
    }
}