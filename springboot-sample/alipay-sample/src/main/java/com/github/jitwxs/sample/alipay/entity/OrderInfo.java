package com.github.jitwxs.sample.alipay.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author jitwxs
 * @since 2018-06-04
 */
@Data
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    @TableId(type = IdType.INPUT)
    private String orderId;
    /**
     * 订单名称
     */
    private String subject;
    /**
     * 订单描述
     */
    private String body;
    /**
     * 付款金额
     */
    private float money;
    /**
     * 商户UID
     */
    private String sellerId;
    /**
     * 支付宝订单号
     */
    private String alipayNo;
    /**
     * 订单状态（与官方统一）
     * WAIT_BUYER_PAY：交易创建，等待买家付款；
     * TRADE_CLOSED：未付款交易超时关闭，或支付完成后全额退款；
     * TRADE_SUCCESS：交易支付成功；
     * TRADE_FINISHED：交易结束，不可退款
     */
    private String status;

    /**
     * 总计退款金额
     */
    private float refundMoney;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改时间
     */
    @TableField(update = "now()")
    private Date updateDate;
}
