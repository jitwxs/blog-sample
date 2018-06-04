package jit.wxs.demo.service;

import jit.wxs.demo.entity.OrderInfo;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-06-04
 */
public interface OrderInfoService extends IService<OrderInfo> {
    /**
     * 生成订单
     * @author jitwxs
     * @since 2018/6/4 16:40
     */
    OrderInfo createOrder(String subject, String body, float money, String sellerId);

    /**
     * 校验订单
     * 支付宝同步/异步回调时调用
     * @author jitwxs
     * @since 2018/6/4 16:40
     */
    boolean validOrder(Map<String,String> params) throws Exception;

    /**
     * 获取订单根据订单ID或者支付交易号
     * @author jitwxs
     * @since 2018/6/4 20:10
     */
    OrderInfo getByIdOrAlipayNo(String orderId, String alipayNo);

    /**
     * 改变订单状态
     * @param tradeNo 支付宝交易号【仅在TRADE_SUCCESS时有效】
     * @author jitwxs
     * @since 2018/6/4 22:42
     */
    boolean changeStatus(String orderId, String status, String... tradeNo);
}
