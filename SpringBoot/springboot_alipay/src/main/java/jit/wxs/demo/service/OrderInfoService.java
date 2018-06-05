package jit.wxs.demo.service;

import com.baomidou.mybatisplus.service.IService;
import jit.wxs.demo.entity.OrderInfo;

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

    /**
     * 手动同步状态
     * 支付宝服务器状态 ---> 数据库状态
     * 调用原因：订单状态没有在支付宝异步通知方法中更新，导致状态不一致。
     *          使用该方法手动同步状态
     * 两个参数传任一即可
     * @param orderId 商户订单号
     * @param alipayNo 支付宝交易号
     * @author jitwxs
     * @since 2018/6/5 21:30
     */
    boolean syncStatus(String orderId, String alipayNo);
}
