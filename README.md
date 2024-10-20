# huava 微服务示例（维护中，待完善）

测试流程：

1. 后端用户登录，添加一件商品。【平台后台微服务】【商品微服务】
2. 前端注册一个新用户，注册的同时完成登录。【会员微服务】
3. 前端查询最新的 16 件商品列表。【商品微服务】
4. 前端查询上一步中所有的商品详情（系统需要保存商品浏览记录）。【商品微服务】【会员微服务】
5. 前端随机选择其中一件商品下单，给用户送积分，在商品库存过低时触发补货（系统需要保存补货记录）。【订单微服务】【会员微服务】【商品微服务】
6. 最后要确保：订单项中卖出的商品数量 + 商品表剩余的商品数量 = 补货记录中的商品数量。【订单微服务】【商品微服务】

---

# 文件夹说明（按字母顺序）

admin：平台后台微服务（管理员用户、角色、权限）。

common-provider：是各微服务依赖的 Jar 包，本身不是微服务。

docs：文档（SQL文件、压测脚本）

gateway：网关

goods：商品微服务

member：会员微服务

order：订单微服务



---

# 重要说明

由于引入了 nacos 相关的包，对 GraalVM native image 的支持不友好（2024-9-16），因此：

不支持 GraalVM native image!

不支持 GraalVM native image!

不支持 GraalVM native image!

---

# 分支说明

`master` 分支：默认分支，基于 Spring Cloud，消息队列用的 Rocket MQ。

`kafka` 分支：把 Rocket MQ 替换为 Kafka。因为总是有面试会问到 Kafka，所以就单独开一个分支体验一把。



---

# Huava Cloud 启动顺序

1. 启动 MySQL，略

---

2. 启动 Redis，略

---

3. 启动 Nacos
```powershell
$host.UI.RawUI.WindowTitle = "na"
cd D:\nacos-server-2.4.2\bin
./startup.cmd -m standalone
```
Nacos 浏览器控制台：
http://localhost:8848/nacos
账号密码：nacos/nacos

---
4. 启动 skywalking （放在前面启动是因为它的端口被占用后不好修改）
```powershell
cd F:\temp\skywalking\skywalking-apm-10.0.1\bin
./oapService.bat
```
等待启动完成（看新窗口中的日志），然后执行：
```powershell
cd F:\temp\skywalking\skywalking-apm-10.0.1\bin
./webappService.bat
```
Skywalking 浏览器控制台：
http://localhost:9685/
注：默认端口是 8080，可以从日志中查看

---

5. 启动 Rocket MQ
   Name Server：
```powershell
$host.UI.RawUI.WindowTitle = "mqns"
cd F:\temp\rocketmq\rocketmq-all-5.3.0\bin
./mqnamesrv.cmd
```
Broker：
```powershell
$host.UI.RawUI.WindowTitle = "mqb"
cd F:\temp\rocketmq\rocketmq-all-5.3.0\bin
./mqbroker.cmd  -n localhost:9876
```
Dashboard：
```powershell
$host.UI.RawUI.WindowTitle = "mqd"
cd F:\temp\rocketmq
java -jar rocketmq-dashboard-2.0.1-SNAPSHOT.jar --server.port=8154
```
Rocket MQ 浏览器控制台：
http://localhost:8154/
注：默认端口是 8080

---

5. 启动 Kafka
```powershell
cd F:\temp\kafka3.8.0
.\bin\windows\kafka-server-start.bat config\kraft\server.properties
```

---

6. 启动 ES
```powershell
$host.UI.RawUI.WindowTitle = "es"
cd F:\temp\elasticsearch\elasticsearch-8.15.2-win64\bin
./elasticsearch.bat
```
通过 Kibana 查看与操作

---

7. 启动 Filebeat
```powershell
$host.UI.RawUI.WindowTitle = "fb"
cd F:\temp\filebeat-8.15.2-win64
./filebeat.exe -e
```

---

8. 启动 Kibana
```powershell
$host.UI.RawUI.WindowTitle = "ki"
cd F:\temp\kibana-8.15.2-win64\bin
./kibana.bat
```
Kibana 浏览器控制台：
http://localhost:5601
账号/密码：elastic/E26Ji5NhDr9ivJwD=kif

---

9. 启动 Seata
```powershell
$host.UI.RawUI.WindowTitle = "sea"
cd F:\temp\seata-2.1.0\bin
./seata-server.bat
```

---

10. 启动 Sentinel
```powershell
$host.UI.RawUI.WindowTitle = "sen"
cd F:\temp\sentinel
java -jar sentinel-dashboard-1.8.8.jar --server.port=23896
```
Sentinel 浏览器控制台：
http://localhost:23896/
账号/密码：sentinel/sentinel
注：由于不能设置成 eager 模式，因此需要访问接口后才能从 Sentinel 浏览器控制台看到对应的应用

---

11. 启动 XXL-Job
```powershell
cd F:\temp\xxl-job-2.4.1\xxl-job-admin\target
java -jar xxl-job-admin-2.4.1.jar --spring.profiles.active=dev
```

---

12. 启动几个微服务：gateway、admin、member、goods、order。

---

# 问答

---

### 1. 为什么 maven 项目不采用父子结构？

采用父子结构的优点是：

1. 可以方便地统一 jar 包的版本。
2. 可以在一个大项目中开发，不用开多个 IDE 。
3. 公共模块不需要 install 就能直接引用到。

采用单一结构的优点是：

1. 可以单独开发某个微服务，不需要打开整个大工程。
2. 某个微服务可以不用 Java 开发。

我个人选取单一结构的核心原因是：我的笔记本电脑是 6 年前的电脑，性能跟不上了，不想一次性打开一个大项目。虽然目前的代码量很少，但是项目总是有增大的趋势，先提前避免。

---
### 2. broker busy

2024-09-29T12:53:47.216+08:00 ERROR 10968 --- [order-service] [    virtual-457] s.b.r.i.o.RocketMQProducerMessageHandler : RocketMQ Message hasn't been sent. Caused by CODE: 2  DESC: [TIMEOUT_CLEAN_QUEUE]broker busy, start flow control for a while, period in queue: 205ms, size of queue: 3 BROKER: localhost:10911
For more information, please visit the url, https://rocketmq.apache.org/docs/bestPractice/06FAQ

参考：https://blog.csdn.net/qq_27641935/article/details/106015319





