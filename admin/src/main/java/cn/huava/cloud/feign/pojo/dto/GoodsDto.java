package cn.huava.cloud.feign.pojo.dto;

import cn.huava.cloud.common.pojo.po.BasePo;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 商品
 *
 * @author Camio1945
 */
@Data
public class GoodsDto extends BasePo {

  /** 商品名称 */
  private String name;

  /** 价格 */
  private BigDecimal price;

  /** 库存 */
  private Integer stock;

  /** 销量 */
  private Integer sales;

  /** 是否上架 */
  private Boolean isOn;

  /** 图片，英文逗号分隔 */
  private String imgs;

  /** 详情 */
  private String detail;
}
