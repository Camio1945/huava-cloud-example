package cn.huava.cloud.order.pojo.qo;

import lombok.Data;

/**
 * 购买商品的参数
 *
 * @author Camio1945
 */
@Data
public class BuyGoodsQo {
  private Long goodsId;
  private int goodsCount;
}
