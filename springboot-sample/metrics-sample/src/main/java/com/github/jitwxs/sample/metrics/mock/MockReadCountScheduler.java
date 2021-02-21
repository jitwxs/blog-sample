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
public class MockReadCountScheduler {

    @Scheduled(initialDelay = 100, fixedDelay = 1000)
    public void mockReadCount() {
        try {
            double value1 = new Random().nextDouble();
            Thread.sleep(new Random().nextInt(1000));
            MetricsUtil.recordCounter(MetricsTagEnum.READ_COUNT_1, value1);

            double value2 = new Random().nextDouble();
            Thread.sleep(new Random().nextInt(1000));
            MetricsUtil.recordCounter(MetricsTagEnum.READ_COUNT_2, value2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}