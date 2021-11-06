[![Gitter](https://badges.gitter.im/jitwxs/blog.svg)](https://gitter.im/jitwxs/blog?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Star](https://img.shields.io/github/stars/jitwxs/blog-sample?color=green)](#)
[![Fork](https://img.shields.io/github/forks/jitwxs/blog-sample)](#)
[![License](https://img.shields.io/github/license/jitwxs/blog-sample?color=orange)](https://opensource.org/licenses/Apache-2.0)

## 从哪里可以找到我

|          |                                                              |
| -------- | ------------------------------------------------------------ |
| 个人网站 | https://jitwxs.cn                                            |
| CSDN     | https://jitwxs.blog.csdn.net                                 |
| 邮箱     | jitwxs@foxmail.com<br>jitwxs@gmail.com                       |
| 微信     | jitwxs <br>![jitwxs](https://cdn.jsdelivr.net/gh/jitwxs/cdn/blog/configuration/wechat_qrcode.png) |

## 目录

### java-sample

| 名称 | 文章地址 |
|:---|:---|
|concurrent-sample|《Java 并发编程》系列|
|dynamic-proxy-sample|[JDK 动态代理与 Cglib 动态代理](https://www.jitwxs.cn/8ee3adf6.html)|
|grpc-sample|[gRPC 在 Java 中的入门实例](https://www.jitwxs.cn/d6535904.html)|
|mock-sample|[👉 跳转子目录](./javase-sample/mock-sample)|
|performance-optimized|[Java 代码性能优化之路](https://www.jitwxs.cn/94186b3a.html)|
|protobuf-sample|[Protobuf 在 Java 中的入门实例](https://www.jitwxs.cn/a5b690ac.html)|
|reflection-sample|反射用法实践|

### springboot-sample

| Modulo                                       | Description                                                  |
| -------------------------------------------- | ------------------------------------------------------------ |
| alipay-sample                                | [Java Web 中接入支付宝支付](https://www.jitwxs.cn/ea57cb90.html) |
| aop-sample                                   | [SpringBoot 整合 AOP](https://www.jitwxs.cn/77bba914.html)   |
| dynamic-schedule-sample                      | [Spring SchedulingConfigurer 实现动态定时任务](https://www.jitwxs.cn/e4d53ddb.html) |
| es-sample                                    | [Elasticsearch 初探（5）——与SpringBoot整合](https://www.jitwxs.cn/79a2adb2.html) |
| i18n-sample                                  | [SprringBoot 配置国际化](https://www.jitwxs.cn/885663.html)  |
| jib-sample                                   | [Google Jib 容器化构建工具](https://www.jitwxs.cn/a526485e.html) |
| [jwt-sample](./springboot-sample/jwt-sample) | [Json Web Token 介绍与基本使用](https://www.jitwxs.cn/7ac4f061.html) |
| metrics-sample                               | [👉 跳转子目录](./springboot-sample/metrics-sample)           |
| mp3-sample                                   | SpringBoot 整合 MyBatisPlus 3.x                              |
| mp-sample                                    | SpringBoot 整合 MyBatisPlus 2.x                              |
| oauth-sample                                 | [Web 三方登录实现（基于OAuth2.0，包含Github和QQ登录，附源码）](https://www.jitwxs.cn/33ad9e35.html) |
| [springboot-security](./springboot-security) | [👉 跳转子目录](./springboot-security)                        |
| shallow-copy-sample                          | [Java 浅拷贝性能比较](https://www.jitwxs.cn/a9fa88a0.html)   |
| shiro-sample                                 | [SpringBoot 集成 Shiro 安全框架](https://www.jitwxs.cn/30819bdf.html) |
| ws-sample                                    | [WebSoket 初探并于 SpringBoot 整合](https://www.jitwxs.cn/9af7a6d1.html) |

### Linux

| 名称 | 文章地址 |
|:---|:---|
|process_comm|[Linux 进程间通信](https://www.jitwxs.cn/6c8041c0.html)|
|io_mode|[Linux IO 模型](https://www.jitwxs.cn/3b3bd025.html)|
|socket|[Linux Socket 编程](https://www.jitwxs.cn/f2ee55a7.html)|

### Vue

| 名称 | 文章地址 |
|:---|:---|
|[vue_axios + vue3_axios](./Vue/vue_axios/README.md)|[Axiso解决跨域访问](https://www.jitwxs.cn/dad1fbe2.html)|

### 其他

| 名称 | 文章地址 |
|:---|:---|
|[Hololens](./Hololens/README.md)|[👉 跳转子目录](./Hololens)|
|[LTP](./LTP/README.md)|《LTP》系列|
|[ActiveMQ](./ActiveMQ/README.md)|《ActiveMQ 初探》系列|

## Java 项目标准化

1. 项目命名采用**全小写，中横线分隔，`sample` 结尾**，包名采用`com.github.jitwxs.sample` 打头，确保每个项目**包名在整个工程中唯一**。例如：*spring-security-sample*，包名 *com.github.jitwxs.sample.springsecurity*。
2. 依赖中含有 SpringBoot 的，作为 `springboot-sample` 项目的一个独立子工程；依赖中不含有 SpringBoot 的，作为 `java-sample` 项目的一个独立子工程。
3. 公共性依赖放置在父工程中，子项目放置本项目特有依赖。
4. 如某文章为系列文章，含有多个章节项目，那么这些章节项目应当作为该系列的子项目，命令后加形如 `-ch01`、`-ch02` 区分。例如：*spring-security-sample-ch01*，包名 *com.github.jitwxs.sample.springsecurity.ch01*。
