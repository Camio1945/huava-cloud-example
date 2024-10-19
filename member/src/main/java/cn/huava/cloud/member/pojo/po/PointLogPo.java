package cn.huava.cloud.member.pojo.po;


import cn.huava.cloud.common.pojo.po.BasePo;
import cn.huava.cloud.common.validation.*;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 积分记录
 *
 * @author Camio1945
 */
@Data
@TableName("point_log")
public class PointLogPo extends BasePo {
  @Serial private static final long serialVersionUID = 1L;

  /** 主键 */
  @TableId private Long id;

  /** 会员id */
  @NotNull(
      message = "会员id不能为空",
      groups = {Create.class, Update.class})
  private Long memberId;

  /** 是否为增加积分：1-增加积分，0-减少积分 */
  @NotNull(
      message = "是否为增加积分不能为空",
      groups = {Create.class, Update.class})
  private Boolean isGaining;

  /** 变动积分 */
  @NotNull(
      message = "变动积分不能为空",
      groups = {Create.class, Update.class})
  private Integer point;

  /** 剩余积分 */
  @NotNull(
      message = "剩余积分不能为空",
      groups = {Create.class, Update.class})
  private Integer remainingPoint;

  /** 创建时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /** 备注 */
  @Size(
      max = 200,
      message = "备注长度不能大于 200 个字符",
      groups = {Create.class, Update.class})
  private String remark;

  /** 详情 */
  @Size(
      max = 2000,
      message = "详情长度不能大于 2000 个字符",
      groups = {Create.class, Update.class})
  private String detail;
}
