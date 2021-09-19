package com.github.jitwxs.sample.mock.service.impl;

import com.github.jitwxs.sample.mock.DomainUtils;
import com.github.jitwxs.sample.mock.client.OrderClient;
import com.github.jitwxs.sample.mock.domain.bean.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
public class OrderServiceImpl2Test {
    @Spy
    private OrderServiceImpl orderService = new OrderServiceImpl();
    @Mock
    private OrderClient orderClient;

    @Test
    public void testCalUserTotalUnDealAmount() {
        final Order order = DomainUtils.randomOrder();

        PowerMockito.when(orderClient.queryUserOrder(Mockito.anyLong(), Mockito.anyList())).thenReturn(Collections.singletonList(order));
        Whitebox.setInternalState(orderService, "orderClient", orderClient);

        final BigDecimal amount = orderService.calUserTotalUnDealAmount(1L);

        assertEquals(order.calUnDealAmount(), amount);
    }
}