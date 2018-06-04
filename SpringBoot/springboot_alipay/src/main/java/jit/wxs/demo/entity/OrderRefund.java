package jit.wxs.demo.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 订单退款
 * </p>
 *
 * @author jitwxs
 * @since 2018-06-04
 */
public class OrderRefund implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退款号
     */
    @TableId(type = IdType.INPUT)
    private String refundId;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 退款金额
     */
    private float money;
    /**
     * 退款账户
     */
    private String account;
    /**
     * 退款原因
     */
    private String reason;
    /**
     * 退款时间
     */
    private String refundDate;

    public OrderRefund() {
    }

    public OrderRefund(String refundId, String orderId, float money, String account, String reason, String refundDate) {
        this.refundId = refundId;
        this.orderId = orderId;
        this.money = money;
        this.account = account;
        this.reason = reason;
        this.refundDate = refundDate;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(String refundDate) {
        this.refundDate = refundDate;
    }

    @Override
    public String toString() {
        return "OrderRefund{" +
        ", refundId=" + refundId +
        ", orderId=" + orderId +
        ", money=" + money +
        ", account=" + account +
        ", reason=" + reason +
        ", refundDate=" + refundDate +
        "}";
    }
}
