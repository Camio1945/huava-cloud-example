package cn.huava.cloud.feign;

import cn.huava.cloud.feign.config.FeignClientConfig;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Camio1945
 */
@FeignClient(name = "member-service", configuration = FeignClientConfig.class)
public interface MemberFeignClient {

  /**
   * 补充余额
   *
   * @param memberId 会员 id
   */
  @PostMapping("/adm/member/auth/replenishBalance")
  void replenishBalance(@NonNull Long memberId);
}
