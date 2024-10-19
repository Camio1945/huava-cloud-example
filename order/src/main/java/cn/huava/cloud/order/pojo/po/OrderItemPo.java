package cn.huava.cloud.order.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单项
 *
 * @author Camio1945
 */
@Data
@TableName("order_item")
public class OrderItemPo {

  @TableId
  private Long id;

  /** 订单ID */
  private Long orderId;

  /** 商品ID */
  private Long goodsId;

  /** 商品数量 */
  private Integer goodsCount;

  /** 商品单价 */
  private BigDecimal goodsPrice;

  /** 商品图片（首图） */
  private String goodsImg;

  /** 商品总金额 */
  private BigDecimal totalAmount;

}
