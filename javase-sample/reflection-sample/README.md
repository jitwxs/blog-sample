## 反射用法

> 欢迎大家补充更多 case

### Field

- 获取字段信息：com.github.jitwxs.sample.reflection.FieldReflectionTest.testListFields
- 获取字段值并重置：com.github.jitwxs.sample.reflection.FieldReflectionTest.testSetCommonField
- 包装类型和基本类型的转换：com.github.jitwxs.sample.reflection.FieldReflectionTest.testPrimitiveAndWrapper
- 获取字段修饰符信息：com.github.jitwxs.sample.reflection.FieldReflectionTest.testFieldProperty

### Method

- 调用公共成员方法：com.github.jitwxs.sample.reflection.MethodReflectionTest.testPublicMethod
- 调用公共静态方法：com.github.jitwxs.sample.reflection.MethodReflectionTest.testPublicStaticMethod
- 调用私有成员方法：com.github.jitwxs.sample.reflection.MethodReflectionTest.testPrivateMethod
- 调用私有静态方法：com.github.jitwxs.sample.reflection.MethodReflectionTest.testPrivateStaticMethod
- 获取方法修饰符信息：com.github.jitwxs.sample.reflection.MethodReflectionTest.testMethodProperty

### Class

- 获取父类信息：com.github.jitwxs.sample.reflection.ClassReflectionTest.testSuperClass
- 获取父类接口信息：com.github.jitwxs.sample.reflection.ClassReflectionTest.testSuperInterface
- 获取父类的泛型类型：com.github.jitwxs.sample.reflection.ClassReflectionTest.testSuperClassGenerics
- 获取父类接口的泛型类型：com.github.jitwxs.sample.reflection.ClassReflectionTest.testSuperInterfaceGenerics