package cn.huava.cloud.admin.service.user;

import cn.huava.cloud.admin.pojo.dto.*;
import cn.huava.cloud.admin.pojo.po.UserPo;
import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.common.pojo.dto.PageDto;
import cn.huava.cloud.common.pojo.qo.PageQo;
import cn.huava.cloud.admin.cache.UserCache;
import cn.huava.cloud.admin.cache.UserRoleCache;
import cn.huava.cloud.admin.mapper.UserMapper;
import cn.huava.cloud.admin.pojo.po.UserExtPo;
import cn.huava.cloud.admin.pojo.qo.LoginQo;
import cn.huava.cloud.admin.pojo.qo.UpdatePasswordQo;
import cn.huava.cloud.common.service.BaseService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AceUserService extends BaseService<UserMapper, UserExtPo> {
  private final LoginService loginService;
  private final RefreshTokenService refreshTokenService;
  private final GetUserInfoService getUserInfoService;
  private final LogoutService logoutService;
  private final UserPageService userPageService;
  private final PasswordEncoder passwordEncoder;
  private final UserCache userCache;
  private final UserRoleCache userRoleCache;

  public UserJwtDto login(@NonNull final HttpServletRequest req, @NonNull final LoginQo loginQo) {
    return loginService.login(req, loginQo);
  }

  public String refreshToken(@NonNull final String refreshToken) {
    return refreshTokenService.refreshToken(refreshToken);
  }

  public void logout(@NonNull final String refreshToken) {
    logoutService.logout(refreshToken);
  }

  public PageDto<UserDto> userPage(@NonNull PageQo<UserExtPo> pageQo, @NonNull UserExtPo params) {
    return userPageService.userPage(pageQo, params);
  }

  public boolean isUsernameExists(Long id, @NonNull String username) {
    return exists(
        Fn.undeletedWrapper(UserExtPo::getDeleteInfo)
          .eq(UserExtPo::getUsername, username)
          .ne(id != null, UserExtPo::getId, id));
  }

  public void updatePassword(@NonNull UpdatePasswordQo updatePasswordQo) {
    UserPo loginUser = Fn.getLoginUser();
    String encodedNewPassword = passwordEncoder.encode(updatePasswordQo.getNewPassword());
    LambdaUpdateWrapper<UserExtPo> wrapper =
        new LambdaUpdateWrapper<UserExtPo>()
            .eq(UserExtPo::getId, loginUser.getId())
            .set(UserExtPo::getPassword, encodedNewPassword);
    update(wrapper);
    userCache.afterSaveOrUpdate((UserExtPo) loginUser);
  }

  public UserInfoDto getUserInfoDto() {
    return getUserInfoService.getUserInfoDto();
  }

  public List<Long> getRoleIdsByUserId(@NonNull Long userId) {
    return userRoleCache.getRoleIdsByUserId(userId);
  }
}
