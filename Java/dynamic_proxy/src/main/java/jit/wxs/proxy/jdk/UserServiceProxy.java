package jit.wxs.proxy.jdk;


import jit.wxs.proxy.service.UserService;
import jit.wxs.proxy.service.impl.UserServiceImpl;

import java.lang.reflect.Proxy;

/**
 * UserService的动态代理类
 * @author jitwxs
 * @since 2018/12/6 15:19
 */
public class UserServiceProxy {
    private UserService userService;

    public UserServiceProxy(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserServiceProxy() {
        // 创建实现InvocationHandler类的对象，将要代理类的对象传进去
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler(userService);

        /*
         * public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)
         * loader : 类加载器（指定当前类即可）
         * interfaces 要代理的类实现的接口列表
         * h：实现具体代理操作的类（要实现InvocationHandler接口）
         */
        UserService userServiceProxy = (UserService) Proxy.newProxyInstance(UserServiceProxy.class.getClassLoader(),
                UserServiceImpl.class.getInterfaces(),
                myInvocationHandler);
        return userServiceProxy;
    }
}