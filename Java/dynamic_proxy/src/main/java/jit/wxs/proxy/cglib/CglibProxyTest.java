package jit.wxs.proxy.cglib;

import jit.wxs.proxy.service.UserService;

/**
 * Cglib 动态代理测试类
 * @author jitwxs
 * @since 2018/12/6 15:34
 */
public class CglibProxyTest {
    public static void main(String[] args) {
        // 创建UserService代理类实例
        UserServiceProxy2 userServiceProxy2 = new UserServiceProxy2();
        // 返回代理后增强过的UserService实例
        UserService usProxy = userServiceProxy2.getUserServiceProxy();

        usProxy.query();
        usProxy.save();
        usProxy.update();
        usProxy.delete();
    }
}
