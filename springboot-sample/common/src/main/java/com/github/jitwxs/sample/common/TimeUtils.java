package com.github.jitwxs.sample.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 时间工具类
 * @author jitwxs
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtils {
    /**
     * @return 当前毫秒数
     */
    public static long nowMs() {
        return System.currentTimeMillis();
    }

    /**
     * 当前毫秒与起始毫秒差
     * @param startMillis 开始纳秒数
     * @return 时间差
     */
    public static long diffMs(long startMillis) {
       return diffMs(startMillis, nowMs());
    }

    /**
     * 计算两个毫秒时间差
     * @param startMillis 开始豪秒数
     * @param endMillis 结束毫秒数
     * @return 时间差
     */
    public static long diffMs(long startMillis, long endMillis) {
        return endMillis - startMillis;
    }

    /**
     * @return 当前纳秒数
     */
    public static long nowNano() {
        return System.nanoTime();
    }

    /**
     * 当前纳秒与开始纳秒差
     * @param startNano 开始纳秒数
     * @return 时间差
     */
    public static long diffNano(long startNano) {
        return diffNano(startNano, nowNano());
    }

    /**
     * 计算两个纳秒时间差
     * @param startNano 开始纳秒数
     * @param endNano 结束纳秒数
     * @return 时间差
     */
    public static long diffNano(long startNano, long endNano) {
        return endNano - startNano;
    }
}