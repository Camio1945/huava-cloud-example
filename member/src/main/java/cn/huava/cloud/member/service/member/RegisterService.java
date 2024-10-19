package cn.huava.cloud.member.service.member;

import cn.huava.cloud.common.pojo.po.BasePo;
import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.member.mapper.MemberMapper;
import cn.huava.cloud.member.pojo.dto.MemberJwtDto;
import cn.huava.cloud.member.pojo.po.MemberPo;
import cn.huava.cloud.member.pojo.po.RegisterPo;
import cn.huava.cloud.member.service.captcha.AceCaptchaService;
import cn.huava.cloud.member.service.jwt.AceJwtService;
import cn.huava.cloud.member.service.refreshtoken.AceRefreshTokenService;
import cn.huava.cloud.member.util.Fn;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 注册（并登录）
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class RegisterService extends BaseService<MemberMapper, MemberPo> {

  private final AceRefreshTokenService sysRefreshTokenAceService;
  private final AceJwtService jwtAceService;
  private final AceCaptchaService aceCaptchaService;
  private final ReplenishBalanceService replenishBalanceService;

  protected MemberJwtDto register(HttpServletRequest req, RegisterPo registerPo) {
    validateCaptcha(req, registerPo);
    beforeSave(registerPo);
    save(registerPo);
    replenishBalanceService.replenishBalance(registerPo.getId());
    MemberJwtDto memberJwtDto = jwtAceService.createToken(registerPo.getId());
    saveRefreshToken(registerPo.getUsername(), memberJwtDto);
    return memberJwtDto;
  }

  private static void beforeSave(RegisterPo registerPo) {
    BasePo.beforeCreate(registerPo);
    registerPo.setPassword(Fn.encryptPassword(registerPo.getPassword()));
    registerPo.setIsEnabled(true);
    registerPo.setBalance(BigDecimal.ZERO);
    registerPo.setPoint(0);
  }

  private void validateCaptcha(HttpServletRequest req, RegisterPo registerPo) {
    Boolean isCaptchaDisabledForTesting = registerPo.getIsCaptchaDisabledForTesting();
    aceCaptchaService.validate(req, registerPo.getCaptchaCode(), isCaptchaDisabledForTesting);
  }

  private void saveRefreshToken(String username, MemberJwtDto memberJwtDto) {
    Wrapper<MemberPo> wrapper =
        new LambdaQueryWrapper<MemberPo>().eq(MemberPo::getUsername, username);
    MemberPo userPo = baseMapper.selectOne(wrapper);
    sysRefreshTokenAceService.saveRefreshToken(userPo.getId(), memberJwtDto.getRefreshToken());
  }
}
