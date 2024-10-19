package cn.huava.cloud.member.service.jwt;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.member.mapper.MemberMapper;
import cn.huava.cloud.member.pojo.dto.MemberJwtDto;
import cn.huava.cloud.member.pojo.po.MemberPo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.json.jwt.JWTUtil;
import org.springframework.stereotype.Service;

/**
 * 创建 token，包含 access token 和 refresh token
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class CreateTokenService extends BaseService<MemberMapper, MemberPo> {

  protected MemberJwtDto createToken(@NonNull Long userId, byte[] jwtKey) {
    return new MemberJwtDto()
        .setAccessToken(createAccessToken(userId, jwtKey))
        .setRefreshToken(createRefreshToken(jwtKey));
  }

  private static String createAccessToken(Long userId, byte[] jwtKey) {
    Map<String, Object> payload = HashMap.newHashMap(3);
    payload.put("sub", userId);
    payload.put("iat", System.currentTimeMillis() / 1000);
    // 1 hour
    payload.put("exp", System.currentTimeMillis() / 1000 + 60 * 60);
    return JWTUtil.createToken(payload, jwtKey);
  }

  private static String createRefreshToken(byte[] jwtKey) {
    Map<String, Object> payload = HashMap.newHashMap(3);
    // 这个 ID 是必须的，否则在同一秒内生成的 refreshToken 可能会相同，但数据要求惟一
    payload.put("id", IdWorker.getIdStr());
    payload.put("iat", System.currentTimeMillis() / 1000);
    // 30 days
    payload.put("exp", System.currentTimeMillis() / 1000 + 30 * 24 * 60 * 60);
    return JWTUtil.createToken(payload, jwtKey);
  }
}
