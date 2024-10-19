package cn.huava.cloud.goods.mq;

import cn.huava.cloud.goods.pojo.po.ReplenishLogPo;
import cn.huava.cloud.goods.service.goods.AceGoodsService;
import cn.huava.cloud.goods.service.replenishlog.AceReplenishLogService;
import java.util.Date;
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

  private final AceGoodsService goodsService;
  private final AceReplenishLogService replenishLogService;

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
      int stock = 100;
      goodsService.updateStockByDelta(goodsId, stock);
      ReplenishLogPo replenishLogPo =
          new ReplenishLogPo().setGoodsId(goodsId).setStock(stock).setCreateTime(new Date());
      replenishLogService.save(replenishLogPo);
    };
  }
}
