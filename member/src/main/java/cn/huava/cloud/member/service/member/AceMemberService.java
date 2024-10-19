package cn.huava.cloud.member.service.member;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.member.cache.MemberCache;
import cn.huava.cloud.member.mapper.MemberMapper;
import cn.huava.cloud.member.mq.UpdateMemberPointMsg;
import cn.huava.cloud.member.pojo.dto.MemberJwtDto;
import cn.huava.cloud.member.pojo.po.MemberPo;
import cn.huava.cloud.member.pojo.po.RegisterPo;
import cn.huava.cloud.member.pojo.qo.DecreaseBalanceQo;
import cn.huava.cloud.member.pojo.qo.LoginQo;
import cn.huava.cloud.member.util.Fn;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员服务主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AceMemberService extends BaseService<MemberMapper, MemberPo> {
  private final LoginService loginService;
  private final RegisterService registerService;
  private final MemberCache memberCache;
  private final DecreaseBalanceService decreaseBalanceService;
  private final UpdateMemberPointService updateMemberPointService;
  private final ReplenishBalanceService replenishBalanceService;

  @Override
  public MemberPo getById(Serializable id) {
    return memberCache.getById((Long) id);
  }

  public MemberJwtDto login(@NonNull HttpServletRequest req, @NonNull LoginQo loginQo) {
    return loginService.login(req, loginQo);
  }

  public boolean isUsernameExists(Long id, @NonNull String username) {
    return exists(
        Fn.undeletedWrapper(MemberPo::getDeleteInfo)
            .eq(MemberPo::getUsername, username)
            .ne(id != null, MemberPo::getId, id));
  }

  @Transactional(rollbackFor = Exception.class)
  public MemberJwtDto register(@NonNull HttpServletRequest req, @NonNull RegisterPo registerPo) {
    return registerService.register(req, registerPo);
  }

  @Transactional(rollbackFor = Exception.class)
  public void decreaseBalance(@NonNull DecreaseBalanceQo decreaseBalanceQo) {
    decreaseBalanceService.decreaseBalance(decreaseBalanceQo);
  }

  public void updateMemberPoint(UpdateMemberPointMsg updateMemberPointMsg) {
    updateMemberPointService.updateMemberPoint(updateMemberPointMsg);
  }

  public void replenishBalance(@NonNull Long memberId) {
    replenishBalanceService.replenishBalance(memberId);
  }
}
