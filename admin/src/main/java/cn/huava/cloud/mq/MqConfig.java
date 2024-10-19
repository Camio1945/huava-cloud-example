package cn.huava.cloud.mq;

import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.feign.GoodsFeignClient;
import java.util.Date;
import java.util.function.Consumer;

import cn.huava.cloud.feign.MemberFeignClient;
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

  private final GoodsFeignClient goodsFeignClient;
  private final MemberFeignClient memberFeignClient;

  /**
   * 与 *.yml（可能是 nacos 中的 *.yml ） 文件中的以下配置对应：
   *
   * <pre>
   * spring.cloud.function.definition
   * spring.cloud.stream.rocketmq.bindings.replenishStockConsumer-in-0
   * spring.cloud.stream.bindings.replenishStockConsumer-in-0
   * </pre>
   *
   * @return
   */
  @Bean
  public Consumer<Message<ReplenishStockMsg>> replenishStockConsumer() {
    return msg -> {
      Long goodsId = msg.getPayload().getGoodsId();
      log.info("收到补货消息，商品ID：{}", goodsId);
      goodsFeignClient.replenishStock(goodsId);
    };
  }

  @Bean
  public Consumer<Message<ReplenishBalanceMsg>> replenishBalanceConsumer() {
    return msg -> {
      Long memberId = msg.getPayload().getMemberId();
      log.info("收到补充余额消息，会员ID：{}", memberId);
      memberFeignClient.replenishBalance(memberId);
    };
  }
}
