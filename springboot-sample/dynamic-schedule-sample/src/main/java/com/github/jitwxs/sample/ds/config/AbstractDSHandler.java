package com.github.jitwxs.sample.ds.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 抽象 Dynamic Schedule 实现，基于 SchedulingConfigurer 实现
 * @author jitwxs
 * @date 2021年03月27日 16:41
 */
@Slf4j
public abstract class AbstractDSHandler<T extends IDSTaskInfo> implements SchedulingConfigurer {

    private DSContainer<T> dsContainer;
    
    private final String CLASS_NAME = getClass().getSimpleName();

    /**
     * 获取用于执行任务的线程池
     */
    protected abstract ExecutorService getWorkerExecutor();

    /**
     * 获取所有的任务信息
     */
    protected abstract List<T> listTaskInfo();

    /**
     * 做具体的任务逻辑
     */
    protected abstract void doProcess(T taskInfo);

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        dsContainer = new DSContainer<>(taskRegistrar);
        // 每隔 100ms 调度一次，用于读取所有任务
        taskRegistrar.addFixedDelayTask(this::scheduleTask, 1000);
    }

    /**
     * 调度任务，加载所有任务并注册
     */
    private void scheduleTask() {
        CollectionUtils.emptyIfNull(listTaskInfo()).forEach(taskInfo ->
                dsContainer.checkTask(taskInfo, new TriggerTask(() ->
                        this.execute(taskInfo), triggerContext -> new CronTrigger(taskInfo.getCron()).nextExecutionTime(triggerContext)
                ))
        );
    }

    private void execute(final T taskInfo) {
        final long taskId = taskInfo.getId();

        try {
            Semaphore semaphore = dsContainer.getSemaphore(taskId);
            if (Objects.isNull(semaphore)) {
                log.error("{} semaphore is null, taskId: {}", CLASS_NAME, taskId);
                return;
            }
            if (semaphore.tryAcquire(3, TimeUnit.SECONDS)) {
                try {
                    getWorkerExecutor().execute(() -> doProcess(taskInfo));
                } finally {
                    semaphore.release();
                }
            } else {
                log.warn("{} too many executor, taskId: {}", CLASS_NAME, taskId);
            }
        } catch (InterruptedException e) {
            log.warn("{} interruptedException error, taskId: {}", CLASS_NAME, taskId);
        } catch (Exception e) {
            log.error("{} execute error, taskId: {}", CLASS_NAME, taskId, e);
        }
    }
}
