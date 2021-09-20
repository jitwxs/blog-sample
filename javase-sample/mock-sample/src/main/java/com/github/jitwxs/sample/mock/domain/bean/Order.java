package com.github.jitwxs.sample.mock.domain.bean;

import com.github.jitwxs.sample.mock.domain.enums.OrderStatusEnum;
import com.github.jitwxs.sample.mock.util.BigDecimalUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jitwxs
 * @date 2021-08-29 21:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private long id;

    private long userId;

    private BigDecimal price;

    private BigDecimal amount;

    private BigDecimal dealAmount;

    private OrderStatusEnum status;

    private Date createTime;

    public final BigDecimal calUnDealAmount() {
        return BigDecimalUtils.subtract(this.amount, this.dealAmount);
    }

    private String print() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", price=" + price +
                ", amount=" + amount +
                ", dealAmount=" + dealAmount +
                ", status=" + status +
                ", createTime=" + createTime +
                "}";
    }
}
