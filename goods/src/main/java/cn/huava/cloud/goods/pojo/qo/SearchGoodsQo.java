package cn.huava.cloud.goods.pojo.qo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 搜索商品的参数
 *
 * @author Camio1945
 */
@Data
public class SearchGoodsQo {
  /** 商品名称 */
  private String name;

  /** 价格区间起始值（含） */
  private BigDecimal startPrice;

  /** 价格区间结束值（含） */
  private BigDecimal endPrice;
}
