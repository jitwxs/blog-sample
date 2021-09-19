package com.github.jitwxs.sample.mock.domain.bean;

import com.github.jitwxs.sample.mock.DomainUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.math.BigDecimal;

@PrepareForTest({Order.class})
@RunWith(PowerMockRunner.class)
public class OrderTest {
    /**
     * 测试 final 方法
     * <p>
     * 需要添加：@RunWith, @PrepareForTest 注解
     * <p>
     * 使用 {@link PowerMockito#mockStatic}
     */
    @Test
    public void testCalUnDealAmount() {
        final Order order = PowerMockito.mock(Order.class);

        PowerMockito.when(order.calUnDealAmount()).thenReturn(BigDecimal.ZERO);

        Assert.assertEquals(BigDecimal.ZERO, order.calUnDealAmount());
    }

    /**
     * 测试 private void 方法
     */
    @Test
    public void testPrintFunc() throws Exception {
        final Order order = PowerMockito.spy(DomainUtils.randomOrder());

        PowerMockito.when(order, "print").thenReturn("hhh");

        final String print = Whitebox.invokeMethod(order, "print");

        Assert.assertTrue("hhh".equals(print));
    }
}