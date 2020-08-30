package com.github.jitwxs.demo;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author jitwxs
 * @date 2020年05月02日 13:58
 */
public class StreamReplaceTest {
    static List<Order> sourceLst = null;

    boolean addPre = true;
    int startAmount = 10;

    public static void main(String[] args) {
        sourceLst = mockOrder(1000000);
        StreamReplaceTest test = new StreamReplaceTest();
        test.calTotalAmount();
        test.calTotalAmountNew();
    }

    public void calTotalAmount() {
        long startTime = Utils.now();

        double buyPre = 0, sellPre = 0;
        if(addPre) {
            buyPre = sourceLst.stream().filter(e -> e.getBuyPreAmount() > startAmount).mapToDouble(Order::getBuyPreAmount).sum();
            sellPre = sourceLst.stream().filter(e -> e.getSellPreAmount() > startAmount).mapToDouble(Order::getSellPreAmount).sum();
        }
        double buy = sourceLst.stream().filter(e -> e.getBuyAmount() > startAmount).mapToDouble(Order::getBuyAmount).sum();
        double sell = sourceLst.stream().filter(e -> e.getSellAmount() > startAmount).mapToDouble(Order::getSellAmount).sum();

        double total = buyPre + sellPre + buy + sell;

        System.out.println("calTotalAmount: " + Utils.diff(startTime));
    }

    public void calTotalAmountNew() {
        long startTime = Utils.now();

        double total = 0;

        for(Order order : sourceLst) {
            if(addPre) {
                if(order.getBuyPreAmount() > startAmount) {
                    total = total +  order.getBuyPreAmount();
                }
                if(order.getSellPreAmount() > startAmount) {
                    total = total + order.getSellPreAmount();
                }
            }
            if(order.getBuyAmount() > startAmount) {
                total = total +  order.getBuyAmount();
            }
            if(order.getSellAmount() > startAmount) {
                total = total +  order.getSellAmount();
            }
        }

        System.out.println("calTotalAmountNew: " + Utils.diff(startTime));
    }

    public static List<Order> mockOrder(int count) {
        List<Order> result = Lists.newArrayListWithCapacity(count);

        IntStream.range(0, count).forEach(e -> result.add(Order.builder()
                .buyAmount(RandomUtils.nextInt(1, 100))
                .sellAmount(RandomUtils.nextInt(1, 100))
                .buyPreAmount(RandomUtils.nextInt(1, 100))
                .sellPreAmount(RandomUtils.nextInt(1, 100)).build()));

        return result;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Order {
        private long id;

        private double buyAmount;

        private double sellAmount;

        private double buyPreAmount;

        private double sellPreAmount;
    }
}
