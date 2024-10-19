package cn.huava.cloud.admin.service.user;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.admin.mapper.UserMapper;
import cn.huava.cloud.admin.pojo.po.RefreshTokenPo;
import cn.huava.cloud.admin.pojo.po.UserExtPo;
import cn.huava.cloud.admin.service.refreshtoken.AceRefreshTokenService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class LogoutService extends BaseService<UserMapper, UserExtPo> {

  private final AceRefreshTokenService aceRefreshTokenService;

  protected void logout(@NonNull final String refreshToken) {
    RefreshTokenPo refreshTokenPo = aceRefreshTokenService.getByRefreshToken(refreshToken);
    if (refreshTokenPo != null) {
      aceRefreshTokenService.softDelete(refreshTokenPo.getId());
    }
  }
}
