package com.github.jitwxs.sample.common;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadPoolUtil {
    private static final long KEEP_ALIVE_TIME = 60L;
    private static final LogUncaughtExceptionHandler logUncaughtExceptionHandler = new LogUncaughtExceptionHandler();

    public static ScheduledThreadPoolExecutor newScheduledExecutor(final int corePoolSize, final Object... name) {
        return new ScheduledThreadPoolExecutor(corePoolSize, new ThreadFactoryBuilder()
                .setNameFormat(StringUtils.join(name, '-'))
                .setUncaughtExceptionHandler(logUncaughtExceptionHandler)
                .setDaemon(true).build());
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            log.warn("ThreadPoolUtil sleep error, ", e);
        }
    }

    /**
     * 确保关闭线程池
     */
    public static void ensureShutdown(final ExecutorService pool) {

        if (pool == null) {
            return;
        }
        pool.shutdownNow();
        try {
            while (!pool.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                ThreadPoolUtil.log.info("still not shutdown");
            }
        } catch (final InterruptedException e) {
            ThreadPoolUtil.log.error("interrepted", e);
        }
        ThreadPoolUtil.log.info("shutdown over");
    }

    /**
     * 线程池构造: 自定义 ThreadFactory name
     */
    public static ThreadPoolExecutor poolBuilder(int core, int max, Object... name) {
        return new ThreadPoolExecutor(core, max, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder()
                        .setNameFormat(StringUtils.join(name, '-'))
                        .setUncaughtExceptionHandler(logUncaughtExceptionHandler)
                        .build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 确保关闭线程池
     */
    public static void ensureShutdownNow(final ExecutorService pool) {
        if (pool == null) {
            return;
        }

        final long start = System.currentTimeMillis();
        pool.shutdown();
        try {
            if (!pool.awaitTermination(200, TimeUnit.MILLISECONDS)) {
                ThreadPoolUtil.log.info("ensureShutdownNow shutdownNow");
                pool.shutdownNow();
                while (!pool.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                    ThreadPoolUtil.log.info("ensureShutdownNow still not shutdown");
                }
            }

        } catch (final Exception e) {
            ThreadPoolUtil.log.error("ensureShutdownNow exception", e);
        }
        ThreadPoolUtil.log.info("ensureShutdownNow shutdown over,time={}", System.currentTimeMillis() - start);
    }


    public static ExecutorService newFixedThreadPool(final String name, final int threads) {
        return Executors.newFixedThreadPool(threads, simleFactory(name));
    }

    public static ScheduledThreadPoolExecutor scheduledThreadPoll(final String name, final int threads) {
        return new ScheduledThreadPoolExecutor(threads, simleFactory(name));
    }


    public static ThreadPoolExecutor newTCoreThreadTimeOutTreadPool(final String name, final int threads, final int idleTime) {
        final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threads,
                threads,
                idleTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                ThreadPoolUtil.simleFactory(name));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    public static ExecutorService newTCoreThreadTimeOutTreadPool(final String name, final int threads, final int idleTime, int size) {
        final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threads,
                threads,
                idleTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue(size),
                ThreadPoolUtil.simleFactory(name),
                (r, executor) -> log.error("thread pool full,name={},size={}", name, size));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    public static ThreadFactory simleFactory(final String name) {
        return new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(final Runnable r) {
                final Thread t = new Thread(r, name + "-" + this.threadNumber.getAndIncrement());
                if (t.isDaemon()) {
                    t.setDaemon(false);
                }
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                return t;
            }
        };
    }

    private static class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(final Thread t, final Throwable e) {
            log.error("Uncaught Exception got, thread: {} ", t.getName(), e);
        }
    }
}