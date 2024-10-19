package cn.huava.cloud.feign;

import cn.huava.cloud.feign.config.FeignClientConfig;
import cn.huava.cloud.member.pojo.po.MemberPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author Camio1945
 */
@FeignClient(name = "member-service", configuration = FeignClientConfig.class)
public interface MemberFeignClient {

  /**
   * 根据 id 查询会员信息
   *
   * @param id 会员 id
   * @return 会员信息
   */
  @GetMapping("/mbr/member/auth/get/{id}")
  MemberPo getMemberById(@PathVariable("id") Long id);

}
