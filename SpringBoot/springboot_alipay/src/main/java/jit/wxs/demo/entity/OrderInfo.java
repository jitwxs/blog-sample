package jit.wxs.demo.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 订单信息表
 * </p>
 *
 * @author jitwxs
 * @since 2018-06-04
 */
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getAlipayNo() {
        return alipayNo;
    }

    public void setAlipayNo(String alipayNo) {
        this.alipayNo = alipayNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(float refundMoney) {
        this.refundMoney = refundMoney;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
