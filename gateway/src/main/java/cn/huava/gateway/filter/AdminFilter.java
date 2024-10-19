package cn.huava.gateway.filter;

import cn.huava.gateway.cache.RoleCache;
import cn.huava.gateway.cache.UserRoleCache;
import cn.huava.gateway.util.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static cn.huava.cloud.common.constant.CommonConstant.SUBJECT_ID_KEY;

/**
 * 平台管理过滤器
 *
 * @author Camio1945
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminFilter implements GlobalFilter, Ordered {
  private static final String URI_AUTH_RANGE_MAIN = "main";

  private static final String[] MAIN_URI_SUFFIX = {"/create", "/delete", "/update", "/page"};

  /** 不能直接注入，因为会产生循环引用。不能直接使用，要用 {@link #getRoleCache()}。 */
  private static RoleCache roleCache;

  /** 不能直接注入，因为会产生循环引用。不能直接使用，要用 {@link #getUserRoleCache()}。 */
  private static UserRoleCache userRoleCache;

  @Value("${project.adminUriAuthRange}")
  private String uriAuthRange;

  /**
   * 优先级，order 越大，优先级越低
   *
   * @return 优先级
   */
  @Override
  public int getOrder() {
    return 2;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
    ServerHttpRequest req = builder.build();
    String rawPath = req.getURI().getRawPath();
    if (rawPath.contains("/admin/") && rawPath.contains("/auth/")) {
      return hasPerm(req)
          .flatMap(
              hasPerm -> {
                if (hasPerm) {
                  return chain.filter(exchange.mutate().request(req).build());
                } else {
                  exchange.getResponse().setRawStatusCode(HttpStatus.SC_UNAUTHORIZED);
                  return exchange.getResponse().setComplete();
                }
              });
    }
    return chain.filter(exchange.mutate().request(req).build());
  }

  private Mono<Boolean> hasPerm(ServerHttpRequest req) {
    String subjectId = req.getHeaders().getFirst(SUBJECT_ID_KEY);
    Long userId = Long.parseLong(subjectId);
    String microServiceUri = GatewayUtil.getMicroServiceUri(req);
    if (!shouldCheckPermission(microServiceUri)) {
      return Mono.just(true);
    }
    Mono<List<Long>> roleIdsMono = getRoleIds(userId);
    return roleIdsMono
        .flatMap(
            roleIds -> {
              boolean hasPerm = false;
              for (Long roleId : roleIds) {
                if (roleId == 1
                    || getRoleCache().getPermUrisByRoleId(roleId).contains(microServiceUri)) {
                  hasPerm = true;
                  break;
                }
              }
              return Mono.just(hasPerm);
            })
        .onErrorResume(
            e -> {
              log.error("hasPerm error", e);
              return Mono.just(false);
            });
  }

  private Mono<List<Long>> getRoleIds(Long userId) {
    return getUserRoleCache().getRoleIdsByUserId(userId);
  }

  /** 见 application.yml 文件中关于 api_auth_range 的注释 */
  private boolean shouldCheckPermission(String uri) {
    if (URI_AUTH_RANGE_MAIN.equals(uriAuthRange)) {
      for (String mainUriSuffix : MAIN_URI_SUFFIX) {
        if (uri.endsWith(mainUriSuffix)) {
          return true;
        }
      }
    }
    return false;
  }

  private RoleCache getRoleCache() {
    if (roleCache == null) {
      roleCache = SingleFlightUtil.execute("roleCache", () -> Fn.getBean(RoleCache.class));
    }
    return roleCache;
  }

  private UserRoleCache getUserRoleCache() {
    if (userRoleCache == null) {
      userRoleCache =
          SingleFlightUtil.execute("userRoleCache", () -> Fn.getBean(UserRoleCache.class));
    }
    return userRoleCache;
  }
}
