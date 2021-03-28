## 如何生成 Protobuf 类

（1）方法一
使用 Maven 插件，同时执行项目自带 protobuf 插件的 `protobuf:compile` 和 `protobuf:compile-custom` 项即可。

（2）方法二
直接使用 Maven install 即可。

## 示例程序

- example1: 入门程序
- example2: 流式响应通信
- example3: 流式请求通信
- example4: 双向流式通信