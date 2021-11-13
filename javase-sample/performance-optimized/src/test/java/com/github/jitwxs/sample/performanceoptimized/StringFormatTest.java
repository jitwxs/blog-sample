package com.github.jitwxs.sample.performanceoptimized;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串格式化性能测试
 *
 * @author jitwxs
 * @date 2020年05月01日 20:46
 */
@RunWith(Parameterized.class)
public class StringFormatTest {
    private LastPriceCache lastPriceCache;
    private LastPriceCacheNew lastPriceCacheNew;

    private static String keySuffix = "last_price_%s";

    @Before
    public void init() {
        lastPriceCache = new LastPriceCache();
        lastPriceCacheNew = new LastPriceCacheNew();
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[3][0]; // repeat count which you want
    }

    @Test
    public void testGetKey() {
        long startTime = Utils.now();
        int i = 500;
        while (i-- >= 0) {
            lastPriceCache.getLastPrice(RandomUtils.nextInt(1, 3));
        }
        System.out.println("testGetKey:" + Utils.diff(startTime));
    }

    @Test
    public void testGetKeyNew() {
        long startTime = Utils.now();
        int i = 500;
        while (i-- >= 0) {
            lastPriceCacheNew.getLastPrice(RandomUtils.nextInt(1, 3));
        }
        System.out.println("testGetKeyNew:" + Utils.diff(startTime));
    }

    static class LastPriceCache {
        public double getLastPrice(int id) {
            String key = this.generatorKey(id);
            Utils.sleep(3);
            return RandomUtils.nextDouble(1, 100);
        }

        private String generatorKey(int id) {
            return String.format(keySuffix, id);
        }
    }

    static class LastPriceCacheNew {
        private Map<Integer, String> keyMap = new HashMap<>();

        public double getLastPrice(int id) {
            String key = generatorKey(id);
            Utils.sleep(3);
            return RandomUtils.nextDouble(1, 100);
        }

        private String generatorKey(int id) {
            String result;
            if (keyMap.containsKey(id)) {
                result = keyMap.get(id);
            } else {
                result = String.format(keySuffix, id);
                keyMap.put(id, result);
            }
            return result;
        }
    }
}