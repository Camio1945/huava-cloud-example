package cn.huava.cloud.order.pojo.qo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 减少余额的参数
 *
 * @author Camio1945
 */
@Data
public class DecreaseBalanceQo {
  /** 扣减金额 */
  private BigDecimal decreaseAmount;

  /** 备注，如：订单支付 */
  private String remark;

  /** 详情，如：{"orderId": "1"} */
  private String detail;
}
