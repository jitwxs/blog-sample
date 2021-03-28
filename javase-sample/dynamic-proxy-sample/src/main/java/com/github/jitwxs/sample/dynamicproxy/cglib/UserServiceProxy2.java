package com.github.jitwxs.sample.dynamicproxy.cglib;

import com.github.jitwxs.sample.dynamicproxy.service.UserService;
import com.github.jitwxs.sample.dynamicproxy.service.impl.UserServiceImpl;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * UserService的动态代理类
 * @author jitwxs
 * @since 2018/12/6 15:22
 */
public class UserServiceProxy2 implements MethodInterceptor {
    public UserService getUserServiceProxy() {
        // 生成代理对象
        Enhancer enhancer = new Enhancer();
        // 设置对谁进行代理
        enhancer.setSuperclass(UserServiceImpl.class);
        // 代理要做什么
        enhancer.setCallback(this);
        // 创建代理对象
        UserService us = (UserService) enhancer.create();

        return us;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("开启事务");
        Object invoke = methodProxy.invokeSuper(o, args);
        System.out.println("提交事务");
        return invoke;
    }
}