package cn.huava.cloud.member.service.member;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.member.cache.MemberCache;
import cn.huava.cloud.member.mapper.MemberMapper;
import cn.huava.cloud.member.pojo.po.BalanceLogPo;
import cn.huava.cloud.member.pojo.po.MemberPo;
import cn.huava.cloud.member.pojo.qo.DecreaseBalanceQo;
import cn.huava.cloud.member.service.balancelog.AceBalanceLogService;
import cn.huava.cloud.member.util.Fn;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import java.math.BigDecimal;
import java.util.Date;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 减少余额
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class DecreaseBalanceService extends BaseService<MemberMapper, MemberPo> {
  private final AceBalanceLogService balanceLogService;
  private final MemberCache memberCache;

  protected void decreaseBalance(@NonNull DecreaseBalanceQo decreaseBalanceQo) {
    MemberPo member = Fn.getLoginMember();
    updateMember(decreaseBalanceQo, member);
    saveBalanceLog(decreaseBalanceQo, member);
  }

  private void saveBalanceLog(DecreaseBalanceQo decreaseBalanceQo, MemberPo member) {
    BigDecimal decreaseAmount = decreaseBalanceQo.getDecreaseAmount();
    BigDecimal remainingBalance = member.getBalance().subtract(decreaseAmount);
    BalanceLogPo balanceLogPo =
        new BalanceLogPo()
            .setMemberId(member.getId())
            .setIsGaining(false)
            .setAmount(decreaseAmount)
            .setRemainingBalance(remainingBalance)
            .setCreateTime(new Date())
            .setRemark(decreaseBalanceQo.getRemark())
            .setDetail(decreaseBalanceQo.getDetail());
    balanceLogService.save(balanceLogPo);
  }

  private void updateMember(DecreaseBalanceQo decreaseBalanceQo, MemberPo member) {
    BigDecimal decreaseAmount = decreaseBalanceQo.getDecreaseAmount();
    LambdaUpdateWrapper<MemberPo> wrapper =
        new LambdaUpdateWrapper<MemberPo>()
            .eq(MemberPo::getId, member.getId())
            .setDecrBy(MemberPo::getBalance, decreaseAmount);
    update(wrapper);
    memberCache.deleteById(member.getId());
  }
}
