package cn.huava.gateway.common.listener;

import cn.huava.gateway.common.util.Fn;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 应用事件监听器
 *
 * @author Camio1945
 */
@Slf4j
@Service
@AllArgsConstructor
public class ApplicationEventListener implements ApplicationListener {

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    System.out.println();
    System.out.println(event.getClass().getName());
    test();
    // 项目启动完成事件
    if (event instanceof ApplicationEnvironmentPreparedEvent) {
      log.info("Environment 环境准备完成");
      test();
      return;
    }
    // 项目启动完成事件
    if (event instanceof ApplicationReadyEvent) {
      log.info("项目启动完成，监听器 " + ApplicationEventListener.class + " 开始执行");
      // test();
      return;
    }
    // 项目停止和应用关闭事件
    if ((event instanceof ContextStoppedEvent) || (event instanceof ContextClosedEvent)) {
      log.info("项目停止和应用关闭事件");
    }
  }

  private void test() {
    System.out.println(Fn.getBean(Environment.class).getProperty("spring.cloud.nacos.discovery.server-addr"));
    System.out.println(Fn.getBean(Environment.class).getProperty("spring.cloud.nacos.config.server-addr"));
    System.out.println("\n");
  }
}
