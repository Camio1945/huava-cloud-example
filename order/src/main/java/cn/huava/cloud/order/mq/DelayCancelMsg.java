package cn.huava.cloud.order.mq;

import lombok.Data;

/**
 * 延时取消订单消息
 *
 * @author Camio1945
 */
@Data
public class DelayCancelMsg {
  private Long orderId;
}
