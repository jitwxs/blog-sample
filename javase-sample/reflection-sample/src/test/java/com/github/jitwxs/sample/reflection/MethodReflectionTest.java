package com.github.jitwxs.sample.reflection;

import com.github.jitwxs.sample.reflection.bean.User;
import com.github.jitwxs.sample.reflection.generics.DataInfo;
import com.github.jitwxs.sample.reflection.service.UserService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 方法相关反射测试
 */
public class MethodReflectionTest {
    /**
     * 反射调用公共成员方法
     */
    @Test
    public void testPublicMethod() throws Exception {
        // .class 方式获取 class 对象
        final Class<UserService> clazz = UserService.class;

        final Method method = clazz.getDeclaredMethod("printUser", User.class);

        UserService userService = new UserService();

        // 由于是非 static 方法，因此第一个参数必须传递对象
        method.invoke(userService, User.random());
    }

    /**
     * 反射调用公共静态方法
     */
    @Test
    public void testPublicStaticMethod() throws Exception {
        // Class.forName 方式获取 class 对象
        final Class<?> clazz = Class.forName("com.github.jitwxs.sample.reflection.service.UserService");

        final Method method = clazz.getDeclaredMethod("randomOne");

        final Object invoke = method.invoke(null);

        System.out.println(invoke);
    }

    /**
     * 反射调用私有成员方法
     */
    @Test
    public void testPrivateMethod() throws Exception {
        final Class<UserService> clazz = UserService.class;

        final Method method = clazz.getDeclaredMethod("saveUser");
        // 由于是私有方法，因此必须要 setAccessible
        method.setAccessible(true);

        UserService userService = new UserService();

        // 由于是非 static 方法，因此第一个参数必须传递对象
        method.invoke(userService);
    }

    /**
     * 反射调用私有静态方法
     */
    @Test
    public void testPrivateStaticMethod() throws Exception {
        final Class<UserService> clazz = UserService.class;

        final Method method = clazz.getDeclaredMethod("privateStaticFunc");
        // 由于是私有方法，因此必须要 setAccessible
        method.setAccessible(true);

        method.invoke(null);
    }

    /**
     * 获取方法修饰符信息
     */
    @Test
    public void testMethodProperty() throws Exception {
        final Class<DataInfo> clazz = DataInfo.class;

        final Method getBeanMethod = clazz.getDeclaredMethod("getBean");
        System.out.printf("DataInfo `getBean()`, isPublic: %s, isInterface: %s, isAbstract: %s\n",
                Modifier.isPublic(getBeanMethod.getModifiers()), Modifier.isInterface(getBeanMethod.getModifiers()), Modifier.isAbstract(getBeanMethod.getModifiers()));

        final Method printMethod = clazz.getDeclaredMethod("print");
        System.out.printf("DataInfo `print()`, isPublic: %s, isInterface: %s, isAbstract: %s\n",
                Modifier.isPublic(printMethod.getModifiers()), Modifier.isInterface(printMethod.getModifiers()), Modifier.isAbstract(printMethod.getModifiers()));

    }
}