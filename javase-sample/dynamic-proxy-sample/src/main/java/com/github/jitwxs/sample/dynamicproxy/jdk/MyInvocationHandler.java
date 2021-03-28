package com.github.jitwxs.sample.dynamicproxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 基于 JDK 的动态代理
 * @author jitwxs
 * @since 2018/12/6 15:18
 */
public class MyInvocationHandler implements InvocationHandler {
    /**
     * 要代理类的对象
     */
    private Object targetObj;

    /**
     * 在创建对象时将要代理类的对象传进来
     */
    public MyInvocationHandler(Object targetObj) {
        this.targetObj = targetObj;
    }

    /**
     * @param proxy  方法对象（没有实际作用）
     * @param method 该方法对象所在的类对象实例
     * @param args   方法参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("开启事务");
        /*
         * obj：调用谁的方法用谁的对象
         * args 方法调用时的参数
         */
        Object invoke = method.invoke(targetObj, args);
        System.out.println("提交事务");

        return invoke;
    }
}