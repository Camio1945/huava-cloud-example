package cn.huava.cloud.feign;

import cn.huava.cloud.feign.config.FeignClientConfig;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Camio1945
 */
@FeignClient(name = "goods-service", configuration = FeignClientConfig.class)
public interface GoodsFeignClient {

  /**
   * 补货
   *
   * @param goodsId 商品ID
   */
  @PostMapping("/adm/goods/auth/replenishStock")
  void replenishStock(@NonNull Long goodsId);
}
