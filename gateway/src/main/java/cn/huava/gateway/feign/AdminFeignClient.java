package cn.huava.gateway.feign;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 * admin 服务的 FeignClient
 *
 * @author Camio1945
 */
@ReactiveFeignClient(name = "admin")
public interface AdminFeignClient {

  /**
   * 根据用户 ID 获取角色 ID 列表
   *
   * @param userId 用户 ID
   * @return 角色 ID 列表
   */
  @GetMapping("/admin/sys/user/auth/getRoleIdsByUserId/{userId}")
  Mono<List<Long>> getRoleIdsByUserId(@PathVariable Long userId);

  /**
   * 根据角色 ID 获取权限 URI 集合
   *
   * @param roleId 角色 ID
   * @return 权限 URI 集合
   */
  @GetMapping("/admin/sys/role/auth/getPermUrisByRoleId/{roleId}")
  Mono<Set<String>> getPermUrisByRoleId(@PathVariable Long roleId);
}
