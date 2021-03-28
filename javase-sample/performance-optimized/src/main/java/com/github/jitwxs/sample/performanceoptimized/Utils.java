package com.github.jitwxs.sample.performanceoptimized;

import java.math.BigDecimal;

/**
 * @author jitwxs
 * @date 2020年05月01日 21:39
 */
public class Utils {
    public static long now() {
        return System.currentTimeMillis();
    }

    public static long diff(long startTime) {
        return now() - startTime;
    }
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static double add(double d1, double d2, double... d3) {
        BigDecimal result = new BigDecimal(String.valueOf(d1));
        result = result.add(new BigDecimal(String.valueOf(d2)));
        if(d3 != null && d3.length > 0) {
            for(double d : d3) {
                result = result.add(new BigDecimal(String.valueOf(d)));
            }
        }
        return result.doubleValue();
    }
}
