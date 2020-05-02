package jit.wxs.performance;

import jit.wxs.performance.util.BeanCopierUtils;
import jit.wxs.performance.util.Utils;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jitwxs
 * @date 2020年05月02日 14:31
 */
public class ShallowCopyTest {
    static Order order = null;
    static int times = 10;

    @BeforeAll
    public static void initOrder() {
        List<User> users = new ArrayList<User>() {{
           add(User.builder().email(RandomStringUtils.randomAlphanumeric(4)).tel(RandomUtils.nextLong(1000, 1000) + "").build());
           add(User.builder().email(RandomStringUtils.randomAlphanumeric(4)).tel(RandomUtils.nextLong(1000, 1000) + "").build());
           add(User.builder().email(RandomStringUtils.randomAlphanumeric(4)).tel(RandomUtils.nextLong(1000, 1000) + "").build());
           add(User.builder().email(RandomStringUtils.randomAlphanumeric(4)).tel(RandomUtils.nextLong(1000, 1000) + "").build());
        }};
        order = Order.builder()
                .amount(RandomUtils.nextInt())
                .name(RandomStringUtils.randomAlphanumeric(5))
                .id(RandomUtils.nextLong())
                .buyerList(users).build();
    }

    @Test
    public void testBeanUtils() {
        long startTime = Utils.now();
        for(int i = 0; i < times; i++) {
            Order orderNew = new Order();
            BeanUtils.copyProperties(order, orderNew);
        }
        System.out.println("testBeanUtils: " + Utils.diff(startTime));
    }

    @Test
    public void testBeanCopierUtils() {
        long startTime = Utils.now();
        for(int i = 0; i < times; i++) {
            Order orderNew = new Order();
            BeanCopierUtils.copyProperties(order, orderNew);
        }
        System.out.println("testBeanCopierUtils: " + Utils.diff(startTime));
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class Order {
        private long id;

        private String name;

        private int amount;

        private List<User> buyerList;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class User {
        private String tel;

        private String email;
    }
}
