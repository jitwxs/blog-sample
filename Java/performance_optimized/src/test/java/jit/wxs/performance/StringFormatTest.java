package jit.wxs.performance;

import jit.wxs.performance.util.Utils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串格式化性能测试
 * @author jitwxs
 * @date 2020年05月01日 20:46
 */
public class StringFormatTest {
    private LastPriceCache lastPriceCache;
    private LastPriceCacheNew lastPriceCacheNew;

    private static String keySuffix = "last_price_%s";

    @BeforeEach
    public void init() {
        lastPriceCache = new LastPriceCache();
        lastPriceCacheNew = new LastPriceCacheNew();
    }

    @RepeatedTest(3)
    public void testGetKey() {
        long startTime = Utils.now();
        int i = 500;
        while (i-- >=0) {
            lastPriceCache.getLastPrice(RandomUtils.nextInt(1, 3));
        }
        System.out.println("testGetKey:" + Utils.diff(startTime));
    }

    @RepeatedTest(3)
    public void testGetKeyNew() {
        long startTime = Utils.now();
        int i = 500;
        while (i-- >=0) {
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
            if(keyMap.containsKey(id)) {
                result = keyMap.get(id);
            } else {
                result = String.format(keySuffix, id);
                keyMap.put(id, result);
            }
            return result;
        }
    }
}