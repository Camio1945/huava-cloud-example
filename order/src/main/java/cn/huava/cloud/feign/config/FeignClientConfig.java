package cn.huava.cloud.feign.config;

import cn.huava.cloud.order.util.Fn;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cn.huava.cloud.common.constant.CommonConstant.*;

/**
 * @author Camio1945
 */
@Slf4j
@Configuration
public class FeignClientConfig {

  @Bean
  public RequestInterceptor requestInterceptor() {
    return template -> {
      HttpServletRequest request = Fn.getRequest();
      template.header(TRACE_ID_KEY, request.getHeader(TRACE_ID_KEY));
      template.header(REQUEST_ID_KEY, request.getHeader(REQUEST_ID_KEY));
      template.header(SUBJECT_ID_KEY, request.getHeader(SUBJECT_ID_KEY));
      template.header(IS_REQUEST_FROM_INNER_NET_KEY, "true");
    };
  }
}
