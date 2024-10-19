package cn.huava.cloud.order.util;

import cn.huava.cloud.common.util.RedisUtil;
import cn.huava.cloud.feign.MemberFeignClient;
import cn.huava.cloud.member.pojo.po.MemberPo;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.json.JSONUtil;

/**
 * 登录工具类
 *
 * @author Camio1945
 */
@Slf4j
public class LoginUtil {
  private LoginUtil() {}

  protected static MemberPo getLoginMember() {
    String subjectId = Fn.getRequest().getHeader("subjectId");
    Long memberId = Long.parseLong(subjectId);
    String key = "str:cache:member:id::" + memberId;
    String jsonStr = RedisUtil.get(key);
    if (Fn.isBlank(jsonStr)) {
      return Fn.getBean(MemberFeignClient.class).getMemberById(memberId);
    } else {
      return JSONUtil.toBean(jsonStr, MemberPo.class);
    }
  }

  protected static Long getLoginMemberId() {
    String subjectId = Fn.getRequest().getHeader("subjectId");
    return Long.parseLong(subjectId);
  }
}
