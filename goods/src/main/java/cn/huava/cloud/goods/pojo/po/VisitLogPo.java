package cn.huava.cloud.goods.pojo.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serial;
import java.util.Date;
import lombok.Data;

/**
 * 商品访问记录
 *
 * @author Camio1945
 */
@Data
@TableName("visit_log")
public class VisitLogPo {
  @Serial private static final long serialVersionUID = 1L;

  /** 主键 */
  @TableId private Long id;

  /** 商品ID */
  private Long goodsId;

  /** 会员ID */
  private Long memberId;

  /** 创建时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;
}
