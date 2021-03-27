package com.github.jitwxs.sample.ds.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * 存放 IDSTaskInfo 容器
 * @author jitwxs
 * @date 2021年03月27日 16:29
 */
@Slf4j
public class DSContainer<T extends IDSTaskInfo> {
    /**
     * IDSTaskInfo和真实任务的关联关系
     *
     * <task_id, <Task, <Scheduled, Semaphore>>>
     */
    private final Map<Long, Pair<T, Pair<ScheduledTask, Semaphore>>> scheduleMap = new ConcurrentHashMap<>();

    private final ScheduledTaskRegistrar taskRegistrar;

    public DSContainer(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        this.taskRegistrar = scheduledTaskRegistrar;
    }

    /**
     * 注册任务
     * @param taskInfo 任务信息
     * @param triggerTask 任务的触发规则
     */
    public void checkTask(final T taskInfo, final TriggerTask triggerTask) {
        final long taskId = taskInfo.getId();

        if (scheduleMap.containsKey(taskId)) {
            if (taskInfo.isValid()) {
                final T oldTaskInfo = scheduleMap.get(taskId).getLeft();

                if(oldTaskInfo.isChange(taskInfo)) {
                    log.info("DSContainer will register again because task config change, taskId: {}", taskId);
                    cancelTask(taskId);
                    registerTask(taskInfo, triggerTask);
                }
            } else {
                log.info("DSContainer will cancelTask because task not valid, taskId: {}", taskId);
                cancelTask(taskId);
            }
        } else {
            if (taskInfo.isValid()) {
                log.info("DSContainer will registerTask, taskId: {}", taskId);
                registerTask(taskInfo, triggerTask);
            }
        }
    }

    /**
     * 获取 Semaphore，确保任务不会被多个线程同时执行
     */
    public Semaphore getSemaphore(final long taskId) {
        return this.scheduleMap.get(taskId).getRight().getRight();
    }

    private void registerTask(final T taskInfo, final TriggerTask triggerTask) {
        final ScheduledTask latestTask = taskRegistrar.scheduleTriggerTask(triggerTask);
        this.scheduleMap.put(taskInfo.getId(), Pair.of(taskInfo, Pair.of(latestTask, new Semaphore(1))));
    }

    private void cancelTask(final long taskId) {
        final Pair<T, Pair<ScheduledTask, Semaphore>> pair = this.scheduleMap.remove(taskId);
        if (pair != null) {
            pair.getRight().getLeft().cancel();
        }
    }
}
