package cn.huava.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import reactor.core.publisher.Mono;

/**
 * @author Camio1945
 */
@Slf4j
@EnableReactiveFeignClients
@SpringBootApplication
public class GatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
    setBlockHandler();
  }

  private static void setBlockHandler() {
    GatewayCallbackManager.setBlockHandler(
        (serverWebExchange, e) -> {
          String msg = "访问量过大，稍后请重试";
          log.info("{} : {}", msg, e.getClass().getSimpleName());
          Map<String, Object> map = HashMap.newHashMap(3);
          map.put("uri", serverWebExchange.getRequest().getURI());
          map.put("msg", msg);
          map.put("code", HttpStatus.TOO_MANY_REQUESTS.value());
          map.put("requestId", serverWebExchange.getRequest().getId());
          return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
              .contentType(MediaType.APPLICATION_JSON)
              .body(Mono.just(map), Map.class);
        });
  }
}
