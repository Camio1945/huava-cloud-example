package cn.huava.cloud.order.pojo.po;

import java.util.List;
import lombok.Data;

/**
 * 订单扩展
 *
 * @author Camio1945
 */
@Data
public class OrderExtPo extends OrderPo {
  /** 订单项列表 */
  private List<OrderItemPo> orderItems;
}
