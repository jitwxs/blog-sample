package com.github.jitwxs.sample.performanceoptimized;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * FastJson预热性能测试
 *
 * @author jitwxs
 * @date 2020年05月02日 10:15
 */
public class FastJsonWarnUpTest {
    static List<TestBean> sourceData = null;
    static int times = 5;

    @BeforeEach
    public void initData() {
        sourceData = mockData(1000);
    }

    @Test
    public void jsonToStr() {
        for (int i = 0; i < times; i++) {
            long start = Utils.now();
            JSON.toJSONString(sourceData);
            System.out.println("jsonToStr: " + Utils.diff(start));
        }
    }

    @Test
    public void jsonToStrNew() {
        // 预热
        warnUp();

        for (int i = 0; i < times; i++) {
            long start = Utils.now();
            JSON.toJSONString(sourceData);
            System.out.println("jsonToStrNew: " + Utils.diff(start));
        }
    }

    public void warnUp() {
        new ParserConfig();
        new SerializeConfig();
    }

    public static List<TestBean> mockData(int size) {
        List<TestBean> beanList = new ArrayList<>(size);
        IntStream.range(0, size).forEach(e -> beanList.add(TestBean.builder().attr(mockMap(RandomUtils.nextInt(20, 100))).build()));
        return beanList;
    }

    public static Map<String, Double> mockMap(int size) {
        Map<String, Double> map = Maps.newHashMapWithExpectedSize(size);
        IntStream.range(0, size).forEach(e -> map.put(RandomStringUtils.randomAlphabetic(5), RandomUtils.nextDouble(1, 1000)));
        return map;
    }


    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestBean {
        private Map<String, Double> attr;
    }
}
