package cn.huava.gateway.util;

import static cn.huava.cloud.common.constant.CommonConstant.TRACE_ID_KEY;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.util.ObjUtil;
import org.slf4j.MDC;
import org.springframework.web.server.ServerWebExchange;

/**
 * 用于把 Sky Walking 的 traceId（tid） 放入 MDC
 *
 * @author Camio1945
 */
@Slf4j
public class SkyWalkingUtil {

  /**
   * tid 放入 MDC
   *
   * @param exchange ServerWebExchange 请求对象
   */
  public static void putTidIntoMdc(ServerWebExchange exchange) {
    putTidIntoMdc(exchange, TRACE_ID_KEY);
  }

  /**
   * tid 放入 MDC
   *
   * @param exchange ServerWebExchange 请求对象
   */
  public static void putTidIntoMdc(ServerWebExchange exchange, String key) {
    try {
      // entrySpanInstance 来自于 Skywalking agent
      Object entrySpanInstance = exchange.getAttributes().get("SKYWALKING_SPAN");
      if (ObjUtil.isEmpty(entrySpanInstance)) {
        return;
      }
      Class<?> entrySpanClazz = entrySpanInstance.getClass().getSuperclass().getSuperclass();
      Field field = entrySpanClazz.getDeclaredField("owner");
      field.setAccessible(true);
      Object ownerInstance = field.get(entrySpanInstance);
      Class<?> ownerClazz = ownerInstance.getClass();
      Method getTraceId = ownerClazz.getMethod("getReadablePrimaryTraceId");
      String tid = (String) getTraceId.invoke(ownerInstance);
      MDC.put(key, "TID:" + tid);
    } catch (Exception e) {
      log.error("gateway 追踪码获取失败", e);
    }
  }
}
