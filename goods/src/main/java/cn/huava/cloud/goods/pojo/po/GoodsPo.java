package cn.huava.cloud.goods.pojo.po;

import cn.huava.cloud.common.pojo.po.BasePo;
import cn.huava.cloud.common.validation.Create;
import cn.huava.cloud.common.validation.Update;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 商品
 *
 * @author Camio1945
 */
@Data
@TableName("goods")
public class GoodsPo extends BasePo {

  /** 商品名称 */
  @NotEmpty(
      message = "商品名称不能为空",
      groups = {Create.class, Update.class})
  private String name;

  /** 价格 */
  private BigDecimal price;

  /** 库存 */
  @NotNull(
      message = "库存不能为空",
      groups = {Create.class, Update.class})
  private Integer stock;

  /** 销量 */
  @NotNull(
      message = "销量不能为空",
      groups = {Create.class, Update.class})
  private Integer sales;

  /** 是否上架 */
  private Boolean isOn;

  /** 图片，英文逗号分隔 */
  private String imgs;

  /** 详情 */
  private String detail;
}
