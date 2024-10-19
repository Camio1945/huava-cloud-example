package cn.huava.cloud.member.service.member;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.member.cache.MemberCache;
import cn.huava.cloud.member.mapper.MemberMapper;
import cn.huava.cloud.member.pojo.po.BalanceLogPo;
import cn.huava.cloud.member.pojo.po.MemberPo;
import cn.huava.cloud.member.service.balancelog.AceBalanceLogService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import java.math.BigDecimal;
import java.util.Date;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 补充余额
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class ReplenishBalanceService extends BaseService<MemberMapper, MemberPo> {
  private static final BigDecimal REPLENISH_AMOUNT = new BigDecimal(10000);

  private final AceBalanceLogService balanceLogService;
  private final MemberCache memberCache;

  protected void replenishBalance(@NonNull Long memberId) {
    MemberPo member = getById(memberId);
    updateMember(member);
    saveBalanceLog(member);
  }

  private void saveBalanceLog(@NonNull MemberPo member) {
    BalanceLogPo balanceLogPo = new BalanceLogPo()
        .setMemberId(member.getId())
        .setIsGaining(true)
        .setAmount(REPLENISH_AMOUNT)
        .setRemainingBalance(member.getBalance().add(REPLENISH_AMOUNT))
        .setCreateTime(new Date())
        .setRemark("管理员充值")
        .setDetail("{}");
    balanceLogService.save(balanceLogPo);
  }

  private void updateMember(@NonNull MemberPo member) {
    LambdaUpdateWrapper<MemberPo> wrapper =
        new LambdaUpdateWrapper<MemberPo>()
            .eq(MemberPo::getId, member.getId())
            .setIncrBy(MemberPo::getBalance, REPLENISH_AMOUNT);
    update(wrapper);
    memberCache.deleteById(member.getId());
  }
}
