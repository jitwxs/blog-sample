package com.github.jitwxs.sample.mock.client;

import com.github.jitwxs.sample.mock.domain.bean.Order;

import java.util.List;

/**
 * 订单 RPC 接口
 *
 * @author jitwxs
 * @date 2021-08-29 21:53
 */
public interface OrderClient {
    Order queryById(final long id);

    /**
     * 根据状态查询用户订单
     */
    List<Order> queryUserOrder(final long userId, final List<Integer> statusList);
}
