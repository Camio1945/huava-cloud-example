package cn.huava.cloud.order.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 支付记录
 * @author Camio1945
 */
@Data
public class PayLogDto {
  @Serial
  private static final long serialVersionUID = 1L;

  /** 主键 */
  @TableId
  private Long id;

  /** 订单ID */
  private Long orderId;

  /** 支付金额 */
  private BigDecimal payAmount;

  /** 创建时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /** 支付方式：1-余额 */
  private Integer payType;
}
