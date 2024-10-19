package cn.huava.gateway.util;

import org.dromara.hutool.core.text.StrUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * 网关工具类
 *
 * @author Camio1945
 */
public class GatewayUtil {
  private GatewayUtil() {}

  /**
   * 从请求路径中获取微服务接口路径
   *
   * @param req 请求对象
   * @return 微服务接口路径
   */
  public static String getMicroServiceUri(ServerHttpRequest req) {
    String path = req.getURI().getRawPath();
    // 获取请求路径的第二个斜杠之后的部分，因为第一个斜杠是微服务名，第二个斜杠是接口路径
    int secondIndexOfSlash = StrUtil.indexOf(path, '/', 1);
    String microServiceUri = path.substring(secondIndexOfSlash);
    return microServiceUri;
  }
}
