---

# 安装步骤

---

### 1. 下载 nacos

去 GitHub 下载 [nacos-server-2.4.2](https://github.com/alibaba/nacos/releases/tag/2.4.2) ，理论上更新版本的也行。

---

### 2. 建库

使用 MySQL 8 的 root 用户执行 sql/nacos-init-2.4.2.sql 文件。

---

### 3. 修改 nacos 配置

修改配置文件：nacos-server-2.4.2/conf/application.properties

放开并修改以下字段，参考值如下：

```properties
spring.sql.init.platform=mysql
db.num=1
db.url.0=jdbc:mysql://127.0.0.1:3306/huava_nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=huava
db.password.0=eFS0H6_0_pkVm__o
```

---

### 4. 单机启动

进入 nacos-server-2.4.2/bin 文件夹，在 cmd 控制台执行：

```cmd
startup.cmd -m standalone
```





