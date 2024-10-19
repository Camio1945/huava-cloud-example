package cn.huava.cloud.admin.service.user;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.admin.service.captcha.AceCaptchaService;
import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.admin.mapper.UserMapper;
import cn.huava.cloud.admin.pojo.dto.UserJwtDto;
import cn.huava.cloud.admin.pojo.po.UserExtPo;
import cn.huava.cloud.admin.pojo.qo.LoginQo;
import cn.huava.cloud.admin.service.jwt.AceJwtService;
import cn.huava.cloud.admin.service.refreshtoken.AceRefreshTokenService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.lang.Assert;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 登录
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class LoginService extends BaseService<UserMapper, UserExtPo> {

  private final AceRefreshTokenService sysRefreshTokenAceService;
  private final AceJwtService jwtAceService;
  private final AceCaptchaService aceCaptchaService;
  private final PasswordEncoder passwordEncoder;

  protected UserJwtDto login(HttpServletRequest req, LoginQo loginQo) {
    validateCaptcha(req, loginQo);
    String username = loginQo.getUsername();
    UserExtPo userExtPo = getUserExtPo(username);
    validateUser(loginQo.getPassword(), userExtPo);
    UserJwtDto userJwtDto = jwtAceService.createToken(userExtPo.getId());
    saveRefreshToken(username, userJwtDto);
    return userJwtDto;
  }

  private void validateCaptcha(HttpServletRequest req, LoginQo loginQo) {
    Boolean isCaptchaDisabledForTesting = loginQo.getIsCaptchaDisabledForTesting();
    aceCaptchaService.validate(req, loginQo.getCaptchaCode(), isCaptchaDisabledForTesting);
  }

  private UserExtPo getUserExtPo(String username) {
    LambdaQueryWrapper<UserExtPo> wrapper = Fn.undeletedWrapper(UserExtPo::getDeleteInfo);
    return getOne(wrapper.eq(UserExtPo::getUsername, username));
  }

  private void validateUser(String password, UserExtPo userExtPo) {
    Assert.isTrue(userExtPo != null, "用户名或密码错误");
    Assert.isTrue(passwordEncoder.matches(password, userExtPo.getPassword()), "用户名或密码错误");
    Assert.isTrue(userExtPo.getIsEnabled(), "用户已被禁用");
  }

  private void saveRefreshToken(String username, UserJwtDto userJwtDto) {
    Wrapper<UserExtPo> wrapper =
        new LambdaQueryWrapper<UserExtPo>().eq(UserExtPo::getUsername, username);
    UserExtPo userPo = baseMapper.selectOne(wrapper);
    sysRefreshTokenAceService.saveRefreshToken(userPo.getId(), userJwtDto.getRefreshToken());
  }
}
