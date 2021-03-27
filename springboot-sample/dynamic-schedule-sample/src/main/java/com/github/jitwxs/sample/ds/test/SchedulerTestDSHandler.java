package com.github.jitwxs.sample.ds.test;

import com.github.jitwxs.sample.ds.handler.AbstractDSHandler;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author jitwxs
 * @date 2021年03月27日 21:54
 */
@Component
public class SchedulerTestDSHandler extends AbstractDSHandler<SchedulerTestTaskInfo> implements ApplicationListener {
    public volatile List<SchedulerTestTaskInfo> taskInfoList = Collections.singletonList(
            SchedulerTestTaskInfo.builder()
                    .id(1)
                    .cron("0/10 * * * * ? ")
                    .isValid(true)
                    .reference("com.github.jitwxs.sample.ds.test.SchedulerTest#foo")
                    .build()
    );

    @Override
    protected ExecutorService getWorkerExecutor() {
        return Executors.newFixedThreadPool(2);
    }

    @Override
    protected List<SchedulerTestTaskInfo> listTaskInfo() {
        return taskInfoList;
    }

    @Override
    protected void doProcess(SchedulerTestTaskInfo taskInfo) {
        final String reference = taskInfo.getReference();
        final String[] split = reference.split("#");
        if(split.length != 2) {
            return;
        }

        final String className = split[0];
        final String funcName = split[1];

       try {
           final Class<?> clazz = Class.forName(className);
           final Method method = clazz.getMethod(funcName);
           method.invoke(clazz.newInstance());
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));

            // setting 1 seconds execute
            taskInfoList = Collections.singletonList(
                    SchedulerTestTaskInfo.builder()
                            .id(1)
                            .cron("0/1 * * * * ? ")
                            .isValid(true)
                            .reference("com.github.jitwxs.sample.ds.test.SchedulerTest#foo")
                            .build()
            );

            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));

            // setting not valid
            taskInfoList = Collections.singletonList(
                    SchedulerTestTaskInfo.builder()
                            .id(1)
                            .cron("0/1 * * * * ? ")
                            .isValid(false)
                            .reference("com.github.jitwxs.sample.ds.test.SchedulerTest#foo")
                            .build()
            );

            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));

            // setting valid
            taskInfoList = Collections.singletonList(
                    SchedulerTestTaskInfo.builder()
                            .id(1)
                            .cron("0/1 * * * * ? ")
                            .isValid(true)
                            .reference("com.github.jitwxs.sample.ds.test.SchedulerTest#foo")
                            .build()
            );
        }, 12, 86400, TimeUnit.SECONDS);
    }
}
