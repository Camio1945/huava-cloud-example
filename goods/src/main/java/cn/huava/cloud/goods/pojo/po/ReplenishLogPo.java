package cn.huava.cloud.goods.pojo.po;

import cn.huava.cloud.common.validation.Create;
import cn.huava.cloud.common.validation.Update;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.util.Date;
import lombok.Data;

/**
 * 补货记录
 *
 * @author Camio1945
 */
@Data
@TableName("replenish_log")
public class ReplenishLogPo {
  @Serial private static final long serialVersionUID = 1L;

  /** 主键 */
  @TableId private Long id;

  /** 商品ID */
  private Long goodsId;

  /** 库存 */
  @NotNull(
      message = "库存不能为空",
      groups = {Create.class, Update.class})
  private Integer stock;

  /** 创建时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;
}
