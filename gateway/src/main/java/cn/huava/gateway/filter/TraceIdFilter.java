package cn.huava.gateway.filter;

import static cn.huava.cloud.common.constant.CommonConstant.REQUEST_ID_KEY;

import cn.huava.gateway.util.SkyWalkingUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.data.id.IdUtil;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Trace Id 过滤器，用于增加 traceId 到日志链路中。<br>
 * 由于 SkyWalking 的 <a href="https://github.com/apache/skywalking/discussions/12675">Bug</a>，网关上的
 * Trace Id 和微服务上的不一致，因此这里还增加了一个 requestId ，在 header 中传递。 <br>
 * 注：这个 filter 必须先于其他的 filter 执行，即 {@link #getOrder()} 的值应该是最低的。
 *
 * @author Camio1945
 */
@Slf4j
@Component
public class TraceIdFilter implements GlobalFilter, Ordered {

  /**
   * 优先级，order 越大，优先级越低
   *
   * @return 优先级
   */
  @Override
  public int getOrder() {
    return 0;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    putRidIntoMdc(exchange);
    // 注：org.apache.skywalking.apm.toolkit.trace.TraceContext.traceId() 不管用
    SkyWalkingUtil.putTidIntoMdc(exchange);
    return chain.filter(exchange);
  }

  private static void putRidIntoMdc(ServerWebExchange exchange) {
    String rid = IdUtil.nanoId(10);
    MDC.put(REQUEST_ID_KEY, "RID:" + rid);
    ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
    ServerHttpRequest req = builder.build();
    builder.header(REQUEST_ID_KEY, rid);
    exchange.mutate().request(req);
  }
}
