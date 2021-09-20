package com.github.jitwxs.sample.mock;

import com.github.jitwxs.sample.mock.domain.bean.Order;
import com.github.jitwxs.sample.mock.domain.enums.OrderStatusEnum;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

/**
 * @author jitwxs
 * @date 2021-08-29 22:07
 */
public class DomainUtils {
    /**
     * 随机生成一笔订单
     */
    public static Order randomOrder() {
        final Order order = Order.builder()
                .id(RandomUtils.nextLong())
                .userId(RandomUtils.nextLong())
                .price(BigDecimal.valueOf(RandomUtils.nextDouble()))
                .amount(BigDecimal.valueOf(RandomUtils.nextInt()))
                .status(Arrays.stream(OrderStatusEnum.values()).findAny().orElse(null))
                .createTime(new Date())
                .build();

        order.setDealAmount(BigDecimal.valueOf(RandomUtils.nextInt(0, order.getAmount().intValue())));

        return order;
    }
}
