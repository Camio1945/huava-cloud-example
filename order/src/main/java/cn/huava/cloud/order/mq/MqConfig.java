package cn.huava.cloud.order.mq;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

/**
 * 消息队列配置
 *
 * @author Camio1945
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MqConfig {

  /**
   * 与 *.yml（可能是 nacos 中的 *.yml ） 文件中的以下配置对应：
   *
   * <pre>
   * spring.cloud.function.definition
   * spring.cloud.stream.rocketmq.bindings.delayCancelOrderConsumer-in-0
   * spring.cloud.stream.bindings.delayCancelOrderConsumer-in-0
   * </pre>
   *
   * @return
   */
  @Bean
  public Consumer<Message<DelayCancelMsg>> delayCancelOrderConsumer() {
    return msg -> {
      Long orderId = msg.getPayload().getOrderId();
      log.info("收到取消订单的延时消息，订单ID：{}", orderId);

      System.out.println("来了来了：" + orderId);
    };
  }
}
