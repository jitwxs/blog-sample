## See Also

- [gRPC 在 Java 中的入门实例](https://www.jitwxs.cn/d6535904.html)
- [The complete gRPC course](https://www.youtube.com/watch?v=2Sm_O75I7H0&list=PLy_6D98if3UJd5hxWNfAqKMr15HZqFnqf)

### How to build protobuf class

（1）方法一
使用 Maven 插件，同时执行项目自带 protobuf 插件的 `protobuf:compile` 和 `protobuf:compile-custom` 项即可。

（2）方法二
直接使用 Maven install 即可。

### Example Case

#### example1

一元（unary）

- 服务端入口：*com.github.jitwxs.sample.grpc.example1.Example1Server.main*
- 客户端入口：*com.github.jitwxs.sample.grpc.example1.Example1Client.main*

### example2

客户端流（client streaming）

- 服务端入口：*com.github.jitwxs.sample.grpc.example1.Example2Server.main*
- 客户端入口：*com.github.jitwxs.sample.grpc.example2.Example2Client.main*

### example3

服务端流（server streaming）

- 服务端入口：*com.github.jitwxs.sample.grpc.example1.Example3Server.main*
- 客户端入口：*com.github.jitwxs.sample.grpc.example2.Example3Client.main*

### example4

双向流（bidirectional streaming）

- 服务端入口：*com.github.jitwxs.sample.grpc.example1.Example4Server.main*
- 客户端入口：*com.github.jitwxs.sample.grpc.example2.Example4Client.main*

### pcbook

综合实例

- 服务端入口：*com.github.jitwxs.sample.grpc.pcbook.LaptopServer.main*
- 客户端入口：*com.github.jitwxs.sample.grpc.pcbook.LaptopClient.main*
  - 一元 *testCreateLaptop*
  - 客户端流 *testSearchLaptop*
  - 服务端流 *testUploadImage*
  - 双向流 *testRateLaptop*
