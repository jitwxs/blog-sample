package com.github.jitwxs.sample.mock.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;

@RunWith(PowerMockRunner.class)
public class BigDecimalUtilsTest {

    /**
     * 测试 static 方法
     * <p>
     * 需要添加：@RunWith, @PrepareForTest 注解
     * <p>
     * 使用 {@link PowerMockito#mockStatic}
     */
    @Test
    @PrepareForTest({BigDecimalUtils.class})
    public void testSubtract() {
        PowerMockito.mockStatic(BigDecimalUtils.class);

        PowerMockito.when(BigDecimalUtils.subtract(Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class))).thenReturn(BigDecimal.ZERO);

        Assert.assertEquals(BigDecimal.ZERO, BigDecimalUtils.subtract(BigDecimal.TEN, BigDecimal.ONE));
    }
}