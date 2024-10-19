package cn.huava.gateway.cache;

import cn.huava.gateway.feign.AdminFeignClient;
import cn.huava.gateway.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import lombok.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 用户拥有的角色缓存
 *
 * @author Camio1945
 */
@Service
@RequiredArgsConstructor
public class UserRoleCache {
  public static final String ROLE_IDS_BY_USER_ID_CACHE_PREFIX = "cache:userRole:userId";

  /** 注：这个 bean 不能直接注入，否则启动的时候会有警告，不会报错，但又会卡死。 */
  private static AdminFeignClient adminFeignClient;

  /**
   * 1. Don't use @Cacheable, otherwise will get this error: <br>
   * SerializationException: Could not read JSON:Unexpected token (START_ARRAY), expected
   * VALUE_STRING: need String, Number of Boolean value that contains type id (for subtype of
   * java.lang.Object)
   */
  public @NonNull Mono<List<Long>> getRoleIdsByUserId(@NonNull Long userId) {
    String key = ROLE_IDS_BY_USER_ID_CACHE_PREFIX + "::" + userId;
    String roleIdsStr = RedisUtil.get(key);
    if (Fn.isBlank(roleIdsStr)) {
      return getAdminFeignClient().getRoleIdsByUserId(userId);
    }
    List<Long> roleIds = Arrays.stream(roleIdsStr.split(",")).map(Long::parseLong).toList();
    return Mono.just(roleIds);
  }

  public AdminFeignClient getAdminFeignClient() {
    if (adminFeignClient == null) {
      adminFeignClient =
          SingleFlightUtil.execute("adminFeignClient", () -> Fn.getBean(AdminFeignClient.class));
    }
    return adminFeignClient;
  }
}
