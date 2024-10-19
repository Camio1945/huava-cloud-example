package cn.huava.cloud.feign.pojo.qo;

import lombok.Data;

/**
 * 减少库存的参数
 *
 * @author Camio1945
 */
@Data
public class DecreaseStockQo {
  private Long goodsId;
  private int goodsCount;
}
