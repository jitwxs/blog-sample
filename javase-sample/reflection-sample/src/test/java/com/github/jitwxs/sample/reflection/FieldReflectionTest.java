package com.github.jitwxs.sample.reflection;

import com.github.jitwxs.sample.reflection.bean.Student;
import com.github.jitwxs.sample.reflection.bean.User;
import com.github.jitwxs.sample.reflection.generics.StudentDataServiceImpl;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 字段相关反射测试
 */
public class FieldReflectionTest {
    /**
     * 获取所有字段
     */
    @Test
    public void testListFields() throws Exception {
        // 1. 反射实现
        for (Field field : User.class.getDeclaredFields()) {
            System.out.printf("name: %s, type: %s\n", field.getName(), field.getType());
        }

        System.out.println("-----");

        // 2. Introspector 实现
        final BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            System.out.printf("name: %s, type: %s\n", descriptor.getName(), descriptor.getPropertyType());
        }
    }

    /**
     * 反射获取并设置字段
     */
    @Test
    public void testSetCommonField() throws Exception {
        final User user = User.random();

        // 1. 反射设置 field 实现
        final Class<User> clazz = User.class;
        final Field field = clazz.getDeclaredField("username");

        // 因为是私有字段，所以要设置 setAccessible
        field.setAccessible(true);
        final Object oldValue = field.get(user);
        final String newValue = RandomStringUtils.random(5);

        field.set(user, newValue);

        System.out.printf("read oldField: %s, setValue: %s, newField: %s\n", oldValue, newValue, field.get(user));

        // 2. 利用 get/set 方法实现
        final BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
            if("username".equals(descriptor.getName())) {
                final Method readMethod = descriptor.getReadMethod();
                final Method writeMethod = descriptor.getWriteMethod();

                final Object oldValue1 = readMethod.invoke(user);
                final String newValue1 = RandomStringUtils.random(5);
                writeMethod.invoke(user, newValue1);

                System.out.printf("read oldField: %s, setValue: %s, newField: %s\n", oldValue1, newValue1, readMethod.invoke(user));
            }
        }
    }

    /**
     * 包装类型和基本数据类型转换
     */
    @Test
    public void testPrimitiveAndWrapper() {
        Class<?> clazz = Student.class;

        /*
         * ClassUtils.primitiveToWrapper 基本 --> 包装
         * ClassUtils.wrapperToPrimitive 包装 --> 基本
         */
        do {
            for (Field field : clazz.getDeclaredFields()) {
                System.out.printf("wrapperClass field: %s, originType: %s, newType: %s\n", field.getName(), field.getType(), ClassUtils.primitiveToWrapper(field.getType()));
            }

            clazz = clazz.getSuperclass();
        } while (clazz != Object.class);
    }

    /**
     * 获取字段修饰符信息
     */
    @Test
    public void testFieldProperty() throws Exception {
        final Class<StudentDataServiceImpl> clazz = StudentDataServiceImpl.class;

        final Field countField = clazz.getDeclaredField("count");
        System.out.printf("StudentDataServiceImpl `count` field, isStatic: %s, isFinal: %s, isVolatile: %s\n",
                Modifier.isStatic(countField.getModifiers()), Modifier.isFinal(countField.getModifiers()), Modifier.isVolatile(countField.getModifiers()));

        final Field classNameField = clazz.getDeclaredField("CLASS_NAME");
        System.out.printf("StudentDataServiceImpl `CLASS_NAME` field, isStatic: %s, isFinal: %s, isVolatile: %s\n",
                Modifier.isStatic(classNameField.getModifiers()), Modifier.isFinal(classNameField.getModifiers()), Modifier.isVolatile(classNameField.getModifiers()));

    }
}