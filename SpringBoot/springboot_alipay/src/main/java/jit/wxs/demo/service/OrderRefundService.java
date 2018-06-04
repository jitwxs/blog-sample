package jit.wxs.demo.service;

import com.baomidou.mybatisplus.service.IService;
import jit.wxs.demo.entity.OrderRefund;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单退款 服务类
 * </p>
 *
 * @author jitwxs
 * @since 2018-06-04
 */
public interface OrderRefundService extends IService<OrderRefund> {
    /**
     * 生成退款信息
     * @param refundId 退款号
     * @param reason 退款原因
     * @param money 退款金额
     * @param map 退款API返回主体Map，不包括sign
     * @author jitwxs
     * @since 2018/6/4 16:40
     */
    OrderRefund createRefund(String refundId, String reason, float money, Map<String, String> map);

    /**
     * 查询退款信息
     * @author jitwxs
     * @since 2018/6/4 22:15
     */
    List<OrderRefund> listRefund(String orderId, String refundId);
}
