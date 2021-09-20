package com.github.jitwxs.sample.mock.util;

import java.math.BigDecimal;

/**
 * @author jitwxs
 * @date 2021-08-29 22:05
 */
public class BigDecimalUtils {
    public static BigDecimal subtract(final BigDecimal a, final BigDecimal b) {
        if (a == null && b == null) {
            return BigDecimal.ZERO;
        }
        if (a == null) {
            return b.negate();
        }
        if (b == null) {
            return a;
        }
        return a.subtract(b);
    }
}
