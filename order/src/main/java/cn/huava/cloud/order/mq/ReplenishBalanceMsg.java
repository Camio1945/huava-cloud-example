package cn.huava.cloud.order.mq;

import lombok.Data;

/**
 * 补充余额消息
 *
 * @author Camio1945
 */
@Data
public class ReplenishBalanceMsg {
  private Long memberId;
}
