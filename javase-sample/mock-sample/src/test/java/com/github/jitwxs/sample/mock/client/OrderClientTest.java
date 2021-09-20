package com.github.jitwxs.sample.mock.client;

import com.github.jitwxs.sample.mock.DomainUtils;
import com.github.jitwxs.sample.mock.domain.bean.Order;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class OrderClientTest {

    /**
     * 测试接口 && 普通方法
     */
    @Test
    public void mockQueryById() {
        final OrderClient orderClient = PowerMockito.mock(OrderClient.class);

        final Order order = DomainUtils.randomOrder();

        PowerMockito.when(orderClient.queryById(Mockito.eq(1L))).thenReturn(order);

        final Order order1 = orderClient.queryById(1L);
        Assert.assertEquals(order, order1);

        final Order order2 = orderClient.queryById(2L);
        Assert.assertNull(order2);
    }
}