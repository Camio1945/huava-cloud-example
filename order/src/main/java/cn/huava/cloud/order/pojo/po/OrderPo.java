package cn.huava.cloud.order.pojo.po;

import cn.huava.cloud.common.pojo.po.BasePo;
import cn.huava.cloud.common.validation.Create;
import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 订单
 *
 * @author Camio1945
 */
@Data
@TableName("`order`")
public class OrderPo extends BasePo {

  /** 订单编号 */
  private String sn;

  /** 会员ID */
  private Long memberId;

  /** 收货地址中的手机号 */
  @NotBlank(
      message = "手机号不能为空",
      groups = {Create.class})
  @Size(
      min = 11,
      max = 14,
      message = "手机号长度必须在11-14之间",
      groups = {Create.class})
  private String shippingMobile;

  /** 收货地址中的姓名 */
  @NotBlank(
      message = "姓名不能为空",
      groups = {Create.class})
  @Size(
      min = 2,
      max = 10,
      message = "姓名长度必须在2-10之间",
      groups = {Create.class})
  private String shippingName;

  /** 收货地址 */
  @NotBlank(
      message = "收货地址不能为空",
      groups = {Create.class})
  @Size(
      max = 100,
      message = "收货地址长度不能超过100",
      groups = {Create.class})
  private String shippingDetail;

  /** 订单总金额 */
  private BigDecimal totalAmount;

  /** 状态：1-待支付，2-待发货，3-待收货，4-已完成，5-已取消 */
  private Integer status;
}
