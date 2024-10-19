package cn.huava.cloud.admin.pojo.po;

import cn.huava.cloud.common.pojo.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Camio1945
 */
@Data
@TableName("refresh_token")
public class RefreshTokenPo extends BasePo {
  private String refreshToken;
  private Long sysUserId;
}
