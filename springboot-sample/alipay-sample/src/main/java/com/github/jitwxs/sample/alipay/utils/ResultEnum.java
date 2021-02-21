package com.github.jitwxs.sample.alipay.utils;

import lombok.Getter;

/**
 * 返回枚举
 * @author jitwxs
 * @since 2018/6/7 10:21
 */
@Getter
public enum ResultEnum {
    OK("正常", 0),
    PARAMS_ERROR("参数错误", 1),
    MONEY_ERROR("金额错误", 2),
    ALIPAY_QUERY_ERROR("支付宝查询错误", 20),
    ALIPAY_REFUND_ERROR("支付宝退款失败", 21),
    ALIPAY_CLOSE_ERROR("支付宝交易关闭失败", 22),
    ORDER_NOT_EXIST("订单不存在",30),
    ORDER_STATUS_NOT_SUPPORT("订单状态不支持",31),;

    private String info;
    private int code;

    ResultEnum(String info, int code) {
        this.info = info;
        this.code = code;
    }

    public static String getMessage(int code) {
        for (ResultEnum enums : ResultEnum.values()) {
            if (enums.getCode() == code) {
                return enums.info;
            }
        }
        return null;
    }
}
