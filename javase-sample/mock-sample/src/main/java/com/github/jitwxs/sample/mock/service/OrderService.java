package com.github.jitwxs.sample.mock.service;

import java.math.BigDecimal;

/**
 * @author jitwxs
 * @date 2021-08-29 21:59
 */
public interface OrderService {
    /**
     * 计算用户累计未完成订单数量
     */
    BigDecimal calUserTotalUnDealAmount(final long userId);
}
