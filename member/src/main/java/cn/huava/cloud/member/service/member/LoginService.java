package cn.huava.cloud.member.service.member;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.member.service.captcha.AceCaptchaService;
import cn.huava.cloud.member.util.Fn;
import cn.huava.cloud.member.mapper.MemberMapper;
import cn.huava.cloud.member.pojo.dto.MemberJwtDto;
import cn.huava.cloud.member.pojo.po.MemberPo;
import cn.huava.cloud.member.pojo.qo.LoginQo;
import cn.huava.cloud.member.service.jwt.AceJwtService;
import cn.huava.cloud.member.service.refreshtoken.AceRefreshTokenService;
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
class LoginService extends BaseService<MemberMapper, MemberPo> {

  private final AceRefreshTokenService sysRefreshTokenAceService;
  private final AceJwtService jwtAceService;
  private final AceCaptchaService aceCaptchaService;
  private final PasswordEncoder passwordEncoder;

  protected MemberJwtDto login(HttpServletRequest req, LoginQo loginQo) {
    validateCaptcha(req, loginQo);
    String username = loginQo.getUsername();
    MemberPo memberPo = getMemberPo(username);
    validateMember(loginQo.getPassword(), memberPo);
    MemberJwtDto memberJwtDto = jwtAceService.createToken(memberPo.getId());
    saveRefreshToken(username, memberJwtDto);
    return memberJwtDto;
  }

  private void validateCaptcha(HttpServletRequest req, LoginQo loginQo) {
    Boolean isCaptchaDisabledForTesting = loginQo.getIsCaptchaDisabledForTesting();
    aceCaptchaService.validate(req, loginQo.getCaptchaCode(), isCaptchaDisabledForTesting);
  }

  private MemberPo getMemberPo(String username) {
    LambdaQueryWrapper<MemberPo> wrapper = Fn.undeletedWrapper(MemberPo::getDeleteInfo);
    return getOne(wrapper.eq(MemberPo::getUsername, username));
  }

  private void validateMember(String password, MemberPo memberPo) {
    Assert.isTrue(memberPo != null, "用户名或密码错误");
    Assert.isTrue(passwordEncoder.matches(password, memberPo.getPassword()), "用户名或密码错误");
    Assert.isTrue(memberPo.getIsEnabled(), "用户已被禁用");
  }

  private void saveRefreshToken(String username, MemberJwtDto memberJwtDto) {
    Wrapper<MemberPo> wrapper =
        new LambdaQueryWrapper<MemberPo>().eq(MemberPo::getUsername, username);
    MemberPo userPo = baseMapper.selectOne(wrapper);
    sysRefreshTokenAceService.saveRefreshToken(userPo.getId(), memberJwtDto.getRefreshToken());
  }
}
