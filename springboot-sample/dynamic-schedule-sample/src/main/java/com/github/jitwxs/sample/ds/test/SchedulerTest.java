package com.github.jitwxs.sample.ds.test;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

/**
 * 模拟动态调整任务执行时间
 * @author jitwxs
 * @date 2021年03月27日 21:52
 */
@Slf4j
public class SchedulerTest {
    public void foo() {
        log.info("{} Execute com.github.jitwxs.sample.ds.test.SchedulerTest#foo", LocalTime.now());
    }
}
