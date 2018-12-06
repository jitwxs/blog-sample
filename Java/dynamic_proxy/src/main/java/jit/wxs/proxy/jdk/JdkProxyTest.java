package jit.wxs.proxy.jdk;

import jit.wxs.proxy.service.UserService;
import jit.wxs.proxy.service.impl.UserServiceImpl;

/**
 * JDK动态代理测试类
 * @author jitwxs
 * @since 2018/12/6 15:21
 */
public class JdkProxyTest {
    public static void main(String[] args) {
        //创建UserService实例
        UserService us = new UserServiceImpl();
        // 创建UserService代理类实例
        UserServiceProxy userServiceProxy = new UserServiceProxy(us);
        // 返回代理后增强过的UserService实例
        UserService usProxy = userServiceProxy.getUserServiceProxy();

        usProxy.delete();
    }
}