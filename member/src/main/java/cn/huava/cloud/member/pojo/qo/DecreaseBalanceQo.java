package cn.huava.cloud.member.pojo.qo;

import cn.huava.cloud.member.validation.DecreaseAmount;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 减少余额的参数
 *
 * @author Camio1945
 */
@Data
public class DecreaseBalanceQo {
  /** 扣减金额 */
  @NotNull(message = "扣减金额不能为空")
  @DecreaseAmount
  private BigDecimal decreaseAmount;

  /** 备注，如：订单支付 */
  @NotBlank(message = "备注不能为空")
  private String remark;

  /** 详情，如：{"orderId": "1"} */
  @NotBlank(message = "详情不能为空")
  private String detail;
}
