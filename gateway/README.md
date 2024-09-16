# 警告

不支持 GraalVM native image.

不支持 GraalVM native image.

不支持 GraalVM native image.

# 遇到的问题

---

### 1. 启动后无法注册到 nacos

解决办法：[参考官方示例项目](https://github.com/alibaba/spring-cloud-alibaba/tree/2023.x/spring-cloud-alibaba-examples/nacos-example)

---

### 2. 路由信息写到 gateway 项目的 application.properties 文件中就有效，但是写到 nacos 的配置中心就无效。

原因：只配置了 discovery ，没有配置 config

解决办法：两个都要配置。参考配置（请酌情修改值）：

```yaml
spring:
  application:
    name: gateway
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        username: 'nacos'
        password: 'nacos'
        namespace: '5e63d532-6a67-403f-b4b4-6e37a3ab57b1'
```

---

### 3. There was an unexpected error (type=Service Unavailable, status=503)

异常详情：

```
Whitelabel Error Page
This application has no configured error view, so you are seeing this as a fallback.
Sun Sep 15 17:05:01 CST 2024
[3c92a7f0-2] There was an unexpected error (type=Service Unavailable, status=503).
```

错误原因：没有引入 spring-cloud-starter-loadbalancer 包

解决办法：在 pom.xml 中引入 spring-cloud-starter-loadbalancer

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

---

### 4. There was an unexpected error (type=Not Found, status=404).

异常详情：

```
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.
Sun Sep 15 17:09:20 CST 2024
There was an unexpected error (type=Not Found, status=404).
```

错误原因 1 ：引入了 spring-cloud-starter-gateway-mvc 依赖，但没有引入 spring-cloud-starter-gateway 依赖

解决办法：把 spring-cloud-starter-gateway-mvc 依赖改为 spring-cloud-starter-gateway 依赖

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

错误原因 2 ：本来应该从 nacos 注册中心拉取的配置实际上没有获取到

解决办法：确认 nacos 是不是存在这个配置项，检查 application.yml 文件中的配置是否正确，比如配置文件的名称、namespace 的值。

---

# 关于 GraalVM native image

踩了一些坑，解决了一个又出现一个，且解决办法越来越不正规，所以决定放弃支持 GraalVM native image。

最后遇到的错误如下：
```log
2024-09-16T13:06:22.389+08:00  INFO 68927 --- [gateway] [remote.worker.1] c.a.n.c.remote.client.grpc.GrpcClient    : grpc client connection server:192.168.0.102 ip,serverPort:9848,grpcTslConfig:{"sslProvider":"","enableTls":false,"mutualAuthEnable":false,"trustAll":false}
2024-09-16T13:06:22.403+08:00  WARN 68927 --- [gateway] [92.168.0.102-35] c.a.n.c.remote.client.grpc.GrpcClient    : [1726463182741_192.168.0.100_48642]Ignore error event,isRunning:false,isAbandon=false
2024-09-16T13:06:22.489+08:00 ERROR 68927 --- [gateway] [           main] com.alibaba.nacos.common.remote.client   : Send request fail, request = InstanceRequest{headers={accessToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTcyNjQ4MTE2Mn0.t8RW73cfj8dPeQwLnKR3DYPJ9ACeysZeD_TJhDmGAkw, app=unknown}, requestId='null'}, retryTimes = 0, errorMessage = Client not connected, current status:STARTING
2024-09-16T13:06:22.590+08:00 ERROR 68927 --- [gateway] [           main] com.alibaba.nacos.common.remote.client   : Send request fail, request = InstanceRequest{headers={accessToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTcyNjQ4MTE2Mn0.t8RW73cfj8dPeQwLnKR3DYPJ9ACeysZeD_TJhDmGAkw, app=unknown}, requestId='null'}, retryTimes = 1, errorMessage = Client not connected, current status:STARTING
2024-09-16T13:06:22.690+08:00 ERROR 68927 --- [gateway] [           main] com.alibaba.nacos.common.remote.client   : Send request fail, request = InstanceRequest{headers={accessToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTcyNjQ4MTE2Mn0.t8RW73cfj8dPeQwLnKR3DYPJ9ACeysZeD_TJhDmGAkw, app=unknown}, requestId='null'}, retryTimes = 2, errorMessage = Client not connected, current status:STARTING
2024-09-16T13:06:22.790+08:00 ERROR 68927 --- [gateway] [           main] com.alibaba.nacos.common.remote.client   : Send request fail, request = InstanceRequest{headers={accessToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTcyNjQ4MTE2Mn0.t8RW73cfj8dPeQwLnKR3DYPJ9ACeysZeD_TJhDmGAkw, app=unknown}, requestId='null'}, retryTimes = 3, errorMessage = Client not connected, current status:STARTING
2024-09-16T13:06:22.791+08:00 ERROR 68927 --- [gateway] [           main] c.a.c.n.registry.NacosServiceRegistry    : nacos registry, gateway register failed...NacosRegistration{nacosDiscoveryProperties=NacosDiscoveryProperties{serverAddr='192.168.0.102:8848', username='nacos', password='nacos', endpoint='', namespace='5e63d532-6a67-403f-b4b4-6e37a3ab57b1', watchDelay=30000, logName='', service='gateway', weight=1.0, clusterName='', group='DEFAULT_GROUP', namingLoadCacheAtStart='false', metadata={preserved.register.source=SPRING_CLOUD}, registerEnabled=true, ip='192.168.0.100', networkInterface='', port=8880, secure=false, accessKey='', secretKey='', heartBeatInterval=null, heartBeatTimeout=null, ipDeleteTimeout=null, instanceEnabled=true, ephemeral=true, failureToleranceEnabled=false}, ipDeleteTimeout=null, failFast=true}},

com.alibaba.nacos.api.exception.NacosException: Client not connected, current status:STARTING
        at com.alibaba.nacos.common.remote.client.RpcClient.request(RpcClient.java:430) ~[gateway:na]
        at com.alibaba.nacos.common.remote.client.RpcClient.request(RpcClient.java:408) ~[gateway:na]
        at com.alibaba.nacos.client.naming.remote.gprc.NamingGrpcClientProxy.requestToServer(NamingGrpcClientProxy.java:447) ~[gateway:na]
        at com.alibaba.nacos.client.naming.remote.gprc.NamingGrpcClientProxy.doRegisterService(NamingGrpcClientProxy.java:250) ~[gateway:na]
        at com.alibaba.nacos.client.naming.remote.gprc.NamingGrpcClientProxy.registerServiceForEphemeral(NamingGrpcClientProxy.java:145) ~[gateway:na]
        at com.alibaba.nacos.client.naming.remote.gprc.NamingGrpcClientProxy.registerService(NamingGrpcClientProxy.java:136) ~[gateway:na]
        at com.alibaba.nacos.client.naming.remote.NamingClientProxyDelegate.registerService(NamingClientProxyDelegate.java:95) ~[na:na]
        at com.alibaba.nacos.client.naming.NacosNamingService.registerInstance(NacosNamingService.java:151) ~[gateway:na]
        at com.alibaba.cloud.nacos.registry.NacosServiceRegistry.register(NacosServiceRegistry.java:75) ~[gateway:na]
        at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.register(AbstractAutoServiceRegistration.java:264) ~[gateway:4.1.4]
        at com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration.register(NacosAutoServiceRegistration.java:78) ~[gateway:na]
        at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.start(AbstractAutoServiceRegistration.java:156) ~[gateway:4.1.4]
        at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.onApplicationEvent(AbstractAutoServiceRegistration.java:119) ~[gateway:4.1.4]
        at org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration.onApplicationEvent(AbstractAutoServiceRegistration.java:49) ~[gateway:4.1.4]
        at org.springframework.context.event.SimpleApplicationEventMulticaster.doInvokeListener(SimpleApplicationEventMulticaster.java:185) ~[na:na]
        at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:178) ~[na:na]
        at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:156) ~[na:na]
        at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:452) ~[gateway:6.1.12]
        at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:385) ~[gateway:6.1.12]
        at org.springframework.boot.web.reactive.context.WebServerManager.start(WebServerManager.java:57) ~[na:na]
        at org.springframework.boot.web.reactive.context.WebServerStartStopLifecycle.start(WebServerStartStopLifecycle.java:41) ~[na:na]
        at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:285) ~[gateway:6.1.12]
        at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:469) ~[gateway:6.1.12]
        at java.base@21.0.4/java.lang.Iterable.forEach(Iterable.java:75) ~[gateway:na]
        at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:257) ~[gateway:6.1.12]
        at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:202) ~[gateway:6.1.12]
        at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:990) ~[gateway:6.1.12]
        at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:628) ~[gateway:6.1.12]
        at org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext.refresh(ReactiveWebServerApplicationContext.java:66) ~[gateway:3.3.3]
        at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754) ~[gateway:3.3.3]
        at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:456) ~[gateway:3.3.3]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:335) ~[gateway:3.3.3]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1363) ~[gateway:3.3.3]
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:1352) ~[gateway:3.3.3]
        at cn.huava.gateway.GatewayApplication.main(GatewayApplication.java:19) ~[gateway:na]
        at java.base@21.0.4/java.lang.invoke.LambdaForm$DMH/sa346b79c.invokeStaticInit(LambdaForm$DMH) ~[na:na]
```
