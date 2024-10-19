package cn.huava.cloud.feign.pojo.qo;

import java.math.BigDecimal;
import lombok.Data;

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
