package cn.huava.cloud.goods.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Configuration
public class XxlJobConfig {
  private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);

  @Value("${xxl.job.admin.addresses}")
  private String adminAddresses;

  @Value("${xxl.job.accessToken}")
  private String accessToken;

  @Value("${xxl.job.executor.appname}")
  private String appname;

  @Value("${xxl.job.executor.address}")
  private String address;

  @Value("${xxl.job.executor.ip}")
  private String ip;

  @Value("${xxl.job.executor.port}")
  private int port;

  @Value("${xxl.job.executor.logpath}")
  private String logPath;

  @Value("${xxl.job.executor.logretentiondays}")
  private int logRetentionDays;

  @Bean
  public XxlJobSpringExecutor xxlJobExecutor() {
    logger.info(">>>>>>>>>>> xxl-job config init.");
    XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
    xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
    xxlJobSpringExecutor.setAppname(appname);
    xxlJobSpringExecutor.setAddress(address);
    xxlJobSpringExecutor.setIp(ip);
    if (port <= 0) {
      port = RandomUtil.randomInt(10000, 65535);
    }
    xxlJobSpringExecutor.setPort(port);
    xxlJobSpringExecutor.setAccessToken(accessToken);
    xxlJobSpringExecutor.setLogPath(logPath);
    xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

    return xxlJobSpringExecutor;
  }

  /**
   * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
   *
   * <p>1、引入依赖：
   *
   * <pre>
   * <dependency>
   *   <groupId>org.springframework.cloud</groupId>
   *   <artifactId>spring-cloud-commons</artifactId>
   *   <version>${version}</version>
   * </dependency>
   * </pre>
   *
   * <p>2、配置文件，或者容器启动变量 spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
   *
   * <p>3、获取IP String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
   */
}
