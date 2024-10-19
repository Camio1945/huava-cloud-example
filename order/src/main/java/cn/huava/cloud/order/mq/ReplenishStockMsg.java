package cn.huava.cloud.order.mq;

import lombok.Data;

/**
 * 商品库存补货消息
 *
 * @author Camio1945
 */
@Data
public class ReplenishStockMsg {
  private Long goodsId;
}
