package com.github.jitwxs.sample.performanceoptimized;

import org.junit.jupiter.api.Test;

/**
 * @author jitwxs
 * @date 2020年05月02日 10:30
 */
public class PackagingTest {
    Integer startPackaging = 0;
    int startBasic = 0;
    static int count = 1000000;

    @Test
    public void testPackaging() {
        long start = Utils.now();
        for (int i = 0; i < count; i++) {
            startPackaging += i;
        }
        System.out.println("testPackaging cost: " + Utils.diff(start));
    }

    @Test
    public void testPackagingNew() {
        long start = Utils.now();
        for (int i = 0; i < count; i++) {
            startBasic += i;
        }
        System.out.println("testPackagingNew cost: " + Utils.diff(start));
    }
}
