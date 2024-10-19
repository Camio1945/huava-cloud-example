package cn.huava.cloud.member.pojo.po;

import cn.huava.cloud.common.pojo.po.BasePo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 刷新令牌
 *
 * @author Camio1945
 */
@Data
@TableName("refresh_token")
public class RefreshTokenPo extends BasePo {
  private String refreshToken;
  private Long memberId;
}
