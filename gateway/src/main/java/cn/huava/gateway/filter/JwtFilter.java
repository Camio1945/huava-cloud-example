package cn.huava.gateway.filter;

import static cn.huava.cloud.common.constant.CommonConstant.SUBJECT_ID_KEY;

import cn.huava.gateway.util.Fn;
import cn.huava.gateway.util.GatewayUtil;
import java.util.Base64;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.dromara.hutool.json.jwt.JWT;
import org.dromara.hutool.json.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * JWT 过滤器。<br>
 *
 * <pre>
 *   1. 如果是可以自由访问的接口，则直接放行；
 *   2. 如果是需要验证的接口，则判断 JWT 是否合法。
 * </pre>
 *
 * @author Camio1945
 */
@Slf4j
@Component
public class JwtFilter implements GlobalFilter, Ordered {
  @Value("${project.jwtKeyBase64}")
  private String jwtKeyBase64;

  /**
   * 优先级，order 越大，优先级越低
   *
   * @return 优先级
   */
  @Override
  public int getOrder() {
    return 1;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
    ServerHttpRequest req = builder.build();
    log.info("网关请求的路径为：{}", req.getURI().getPath());
    // 注：要考虑到有一种特殊的接口，登不登录都可以访问，比如商品详情。如果登录了，就要把用户信息传递下去。
    // 验证 JWT
    boolean isTokenLegal = isTokenLegal(req, builder);
    // 可以自由访问的接口直接放行
    if (isFreeUri(req)) {
      return chain.filter(exchange.mutate().request(req).build());
    }
    // 令牌不合法，返回 401
    if (!isTokenLegal) {
      exchange.getResponse().setRawStatusCode(HttpStatus.SC_UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }
    return chain.filter(exchange.mutate().request(req).build());
  }

  /** Token 是否合法 */
  private boolean isTokenLegal(ServerHttpRequest req, ServerHttpRequest.Builder builder) {
    String token = getToken(req);
    boolean isTokenLegal = false;
    if (Fn.isNotBlank(token)) {
      isTokenLegal = isTokenLegal(token);
      if (isTokenLegal) {
        // 可能是后台的管理员 userId ，也可能是前台的普通用户 memberId
        Object subjectId = JWTUtil.parseToken(token).getPayload("sub");
        builder.header(SUBJECT_ID_KEY, subjectId.toString());
      }
    }
    return isTokenLegal;
  }

  private static String getToken(ServerHttpRequest req) {
    String authorization = req.getHeaders().getFirst("Authorization");
    authorization = authorization == null ? "" : authorization;
    return authorization.replace("Bearer ", "");
  }

  /**
   * 判断给定的 URI 是否为可以自由访问的 URI
   *
   * @param req 请求对象
   * @return 如果给定的URI以任意不需要鉴权的 URI 前缀开始，则返回true；否则返回false
   */
  private boolean isFreeUri(@NonNull ServerHttpRequest req) {
    String microServiceUri = GatewayUtil.getMicroServiceUri(req);
    return microServiceUri.contains("/free/");
  }

  /**
   * 判断令牌是否合法
   *
   * @param token 令牌
   * @return true：合法；false：不合法
   */
  private boolean isTokenLegal(String token) {
    return JWTUtil.verify(token, getJwtKeyBytes()) && !isTokenExpired(token);
  }

  /**
   * 判断令牌是否过期
   *
   * @param token 令牌
   * @return true：过期；false：未过期
   */
  private boolean isTokenExpired(@NonNull final String token) {
    JWT jwt = JWTUtil.parseToken(token);
    Long exp = jwt.getPayload("exp", Long.class);
    return exp != null && exp * 1000 <= System.currentTimeMillis();
  }

  private byte[] getJwtKeyBytes() {
    return Base64.getDecoder().decode(jwtKeyBase64);
  }
}
