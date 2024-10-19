package cn.huava.cloud.member.service.jwt;

import cn.huava.cloud.common.constant.CommonConstant;
import cn.huava.cloud.member.pojo.dto.MemberJwtDto;
import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.json.jwt.JWT;
import org.dromara.hutool.json.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * JWT 服务主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AceJwtService {
  private final CreateTokenService createTokenService;

  @Value("${project.jwt_key_base64}")
  private String jwtKeyBase64;

  public MemberJwtDto createToken(@NonNull Long userId) {
    return createTokenService.createToken(userId, getJwtKeyBytes());
  }

  public Long getUserIdFromAccessToken(@NonNull final String token) {
    return JWTUtil.parseToken(token).getPayload("sub", Long.class);
  }

  public boolean isTokenExpired(@NonNull final String token) {
    Assert.isTrue(JWTUtil.verify(token, getJwtKeyBytes()), "invalid token");
    JWT jwt = JWTUtil.parseToken(token);
    Long exp = jwt.getPayload("exp", Long.class);
    return exp != null && exp * CommonConstant.MILLIS_PER_SECOND <= System.currentTimeMillis();
  }

  private byte[] getJwtKeyBytes() {
    return Base64.getDecoder().decode(jwtKeyBase64);
  }
}
