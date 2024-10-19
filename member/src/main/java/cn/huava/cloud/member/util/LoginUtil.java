package cn.huava.cloud.member.util;

import static cn.huava.cloud.member.cache.MemberCache.MEMBER_ID_CACHE_PREFIX;

import cn.huava.cloud.common.util.RedisUtil;
import cn.huava.cloud.member.pojo.po.MemberPo;
import cn.huava.cloud.member.service.member.AceMemberService;

/**
 * 登录工具类
 *
 * @author Camio1945
 */
class LoginUtil {
  private LoginUtil() {}

  protected static MemberPo getLoginMember() {
    String subjectId = Fn.getRequest().getHeader("subjectId");
    Long memberId = Long.parseLong(subjectId);
    String key = MEMBER_ID_CACHE_PREFIX + "::" + memberId;
    MemberPo memberPo = RedisUtil.get(key);
    if (memberPo == null) {
      memberPo = Fn.getBean(AceMemberService.class).getById(memberId);
    }
    return memberPo;
  }
}
