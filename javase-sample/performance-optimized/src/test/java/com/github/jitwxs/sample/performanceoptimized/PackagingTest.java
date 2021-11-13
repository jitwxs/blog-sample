package com.github.jitwxs.sample.performanceoptimized;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author jitwxs
 * @date 2020年05月02日 10:30
 */
@RunWith(Parameterized.class)
public class PackagingTest {
    Integer startPackaging = 0;
    int startBasic = 0;
    static int count = 1000000;

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[3][0]; // repeat count which you want
    }

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
