package com.github.jitwxs.sample.reflection;

import com.github.jitwxs.sample.reflection.bean.Student;
import com.github.jitwxs.sample.reflection.bean.User;
import com.github.jitwxs.sample.reflection.generics.StudentDataServiceImpl;
import com.github.jitwxs.sample.reflection.generics.UserDataInfo;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * 类相关反射测试
 * @author jitwxs
 * @date 2021年03月28日 19:46
 */
public class ClassReflectionTest {
    /**
     * 获取父类信息
     */
    @Test
    public void testSuperClass() {
        final Class<Student> clazz = Student.class;
        final Class<? super Student> superclass = clazz.getSuperclass();
        System.out.printf("%s super class is %s", clazz, superclass);
    }

    /**
     * 获取父类接口
     */
    @Test
    public void testSuperInterface() {
        final Class<User> clazz = User.class;
        final Class<?>[] interfaces = clazz.getInterfaces();
        System.out.printf("%s super interface is %s", clazz, Arrays.asList(interfaces));
    }

    /**
     * 获取父类的泛型类型
     */
    @Test
    public void testSuperClassGenerics() {
        final Class<UserDataInfo> clazz = UserDataInfo.class;

        final Type type = clazz.getGenericSuperclass();

        // 泛型属于 ParameterizedType，需要做类型转换
        if(type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;

            System.out.println("父类的自身类型是：" + parameterizedType.getRawType());

            System.out.println("父类的泛型是：" + Arrays.asList(parameterizedType.getActualTypeArguments()));
        }
    }

    /**
     * 获取父类接口的泛型类型
     */
    @Test
    public void testSuperInterfaceGenerics() {
        final Class<StudentDataServiceImpl> clazz = StudentDataServiceImpl.class;

        for (Type type : clazz.getGenericInterfaces()) {
            // 泛型属于 ParameterizedType，需要做类型转换
            if(type instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType) type;

                System.out.println("父类的自身类型是：" + parameterizedType.getRawType());

                System.out.println("父类的泛型是：" + Arrays.asList(parameterizedType.getActualTypeArguments()));
            }
        }
    }
}
