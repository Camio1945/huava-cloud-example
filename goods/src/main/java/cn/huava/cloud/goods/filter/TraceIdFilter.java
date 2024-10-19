package cn.huava.cloud.goods.filter;

import static cn.huava.cloud.common.constant.CommonConstant.REQUEST_ID_KEY;
import static cn.huava.cloud.common.constant.CommonConstant.TRACE_ID_KEY;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Trace Id 过滤器，用于增加 traceId 到日志链路中。<br>
 *
 * @author Camio1945
 */
@Slf4j
@Component
public class TraceIdFilter extends OncePerRequestFilter implements Ordered {

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
  protected void doFilterInternal(
      @NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain)
      throws ServletException, IOException {
    try {
      MDC.put(TRACE_ID_KEY, "TID:" + TraceContext.traceId());
      MDC.put(REQUEST_ID_KEY, "RID:" + request.getHeader(REQUEST_ID_KEY));
      log.info("goods : {}", request.getRequestURI());
    } catch (Exception e) {
      // 这里的异常不会影响业务，所以直接打印日志消息，不打印堆栈
      log.error("traceIdFilter error", e.getMessage());
    }
    filterChain.doFilter(request, response);
  }

}
