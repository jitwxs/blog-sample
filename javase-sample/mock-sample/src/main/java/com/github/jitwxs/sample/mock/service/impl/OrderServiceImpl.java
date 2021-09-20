package com.github.jitwxs.sample.mock.service.impl;

import com.github.jitwxs.sample.mock.client.OrderClient;
import com.github.jitwxs.sample.mock.domain.bean.Order;
import com.github.jitwxs.sample.mock.service.OrderService;
import com.github.jitwxs.sample.mock.util.BigDecimalUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.github.jitwxs.sample.mock.domain.enums.OrderStatusEnum.NOT_DEAL;
import static com.github.jitwxs.sample.mock.domain.enums.OrderStatusEnum.PART_DEAL;

/**
 * @author jitwxs
 * @date 2021-08-29 22:02
 */
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderClient orderClient;

    @Override
    public BigDecimal calUserTotalUnDealAmount(long userId) {
        final List<Order> orderList = orderClient.queryUserOrder(userId, Arrays.asList(NOT_DEAL.getStatus(), PART_DEAL.getStatus()));

        return orderList.stream()
                .map(e -> BigDecimalUtils.subtract(e.getAmount(), e.getDealAmount())).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }
}