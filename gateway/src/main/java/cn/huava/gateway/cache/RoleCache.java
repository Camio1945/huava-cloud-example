package cn.huava.gateway.cache;

import cn.huava.gateway.feign.AdminFeignClient;
import cn.huava.gateway.util.Fn;
import cn.huava.gateway.util.SingleFlightUtil;
import java.util.Set;
import lombok.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 角色缓存
 *
 * @author Camio1945
 */
@Service
@RequiredArgsConstructor
public class RoleCache {
  public static final String URIS_CACHE_PREFIX = "cache:role:uris:roleId";
  private static AdminFeignClient adminFeignClient;

  @Cacheable(value = URIS_CACHE_PREFIX, key = "#roleId")
  public Set<String> getPermUrisByRoleId(@NonNull Long roleId) {
    return getAdminFeignClient().getPermUrisByRoleId(roleId).block();
  }

  public AdminFeignClient getAdminFeignClient() {
    if (adminFeignClient == null) {
      adminFeignClient = SingleFlightUtil.execute("adminFeignClient", () -> Fn.getBean(AdminFeignClient.class));
    }
    return adminFeignClient;
  }
}
