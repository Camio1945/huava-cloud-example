
---

# 说明

把 [huava](https://github.com/Camio1945/huava) 项目搬过来，改造成一个微服务。

---

# 从单体改成微服务笔记

---

### 1. 引入 nacos jar 包

```xml
<!-- 微服务的注册发现 -->
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
  <version>2023.0.1.2</version>
</dependency>

<!-- 微服务的动态配置 -->
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
  <version>2023.0.1.2</version>
</dependency>
```

---

### 2. application*.yml 改动

1. 把原先的配置都挪到 nacos 配置中心。

2. application*.yml 中只保留 nacos 的相关配置。


---

### 3. 删除跨域配置

因为跨域的配置统一放到网关服务中实现了，微服务里面如果不删的话，可能会产生如下错误：

```text
Access to XMLHttpRequest at 'http://localhost:8888/admin/sys/user/login' from origin 'http://localhost:5173' has been blocked by CORS policy: The 'Access-Control-Allow-Origin' header contains multiple values 'http://localhost:5173, *', but only one is allowed.
```

---

### 
