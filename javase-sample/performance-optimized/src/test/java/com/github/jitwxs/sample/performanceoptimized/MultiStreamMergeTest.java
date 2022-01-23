package com.github.jitwxs.sample.performanceoptimized;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 多个Stream流合并性能测试
 *
 * @author jitwxs
 * @date 2020年05月01日 21:47
 */
public class MultiStreamMergeTest {
    static List<Order> sourceOrder;

    static long startId, endId;
    static int limit;
    static boolean isAsc = false;

    @BeforeEach
    public void initOrder() {
        sourceOrder = mockOrder(100_0000);
        startId = 10;
        endId = 50;
        limit = 25;
    }

    @Test
    public void queryOrder() {
        long start = Utils.now();

        List<Order> result = sourceOrder.stream().filter(e -> e.getAmount() > 0).collect(Collectors.toList());

        if (startId > 0) {
            result = result.stream().filter(e -> e.getId() >= startId).collect(Collectors.toList());
        }
        if (endId > 0) {
            result = result.stream().filter(e -> e.getId() < endId).collect(Collectors.toList());
        }

        if (result.size() > limit) {
            result = result.subList(0, limit);
        }

        Collections.reverse(result);

        System.out.println("queryOrder: " + Utils.diff(start));
    }

    @Test
    public void queryOrderNew() {
        long start = Utils.now();

        Stream<Order> stream = sourceOrder.stream();

        stream = stream.filter(e -> e.getAmount() > 0);

        if (startId > 0) {
            stream = stream.filter(e -> e.getId() >= startId);
        }
        if (endId > 0) {
            stream = stream.filter(e -> e.getId() < endId);
        }

        Comparator<Order> comparator = Comparator.comparingLong(Order::getId);
        if (!isAsc) {
            comparator = comparator.reversed();
        }

        List<Order> result = stream.limit(limit).sorted(comparator).collect(Collectors.toList());

        System.out.println("queryOrderNew: " + Utils.diff(start));
    }

    public static List<Order> mockOrder(int count) {
        List<Order> result = Lists.newArrayListWithCapacity(count);

        IntStream.range(0, count).forEach(e -> result.add(Order.builder()
                .id(RandomUtils.nextLong(1, 100))
                .amount(RandomUtils.nextInt(1, 1000)).build()));

        return result;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Order {
        private long id;

        private int amount;
    }
}
