package cn.huava.cloud.member.service.member;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.member.mapper.MemberMapper;
import cn.huava.cloud.member.mq.UpdateMemberPointMsg;
import cn.huava.cloud.member.pojo.po.*;
import cn.huava.cloud.member.service.pointlog.AcePointLogService;
import cn.huava.cloud.member.util.Fn;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 更新会员积分<br>
 * 1. 修改 member 表的积分字段<br>
 * 2. 新增 point_log 表的记录
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class UpdateMemberPointService extends BaseService<MemberMapper, MemberPo> {
  private final AcePointLogService pointLogService;

  protected void updateMemberPoint(UpdateMemberPointMsg updateMemberPointMsg) {
    MemberPo member = getById(updateMemberPointMsg.getMemberId());
    // 更新积分
    updatePoint(updateMemberPointMsg);
    // 新增积分记录
    savePointLog(updateMemberPointMsg, member);
  }

  private void updatePoint(UpdateMemberPointMsg updateMemberPointMsg) {
    Long memberId = updateMemberPointMsg.getMemberId();
    Integer point = updateMemberPointMsg.getPoint();
    LambdaUpdateWrapper<MemberPo> wrapper =
        new LambdaUpdateWrapper<MemberPo>()
            .eq(MemberPo::getId, memberId)
            .setIncrBy(MemberPo::getPoint, point);
    update(wrapper);
  }

  private void savePointLog(UpdateMemberPointMsg updateMemberPointMsg, MemberPo member) {
    Integer point = updateMemberPointMsg.getPoint();
    PointLogPo pointLogPo =
        new PointLogPo()
            .setMemberId(updateMemberPointMsg.getMemberId())
            .setIsGaining(point > 0)
            .setPoint(point)
            .setRemainingPoint(member.getPoint() - point)
            .setCreateTime(new Date())
            .setRemark(updateMemberPointMsg.getRemark())
            .setDetail(updateMemberPointMsg.getDetail());
    pointLogService.save(pointLogPo);
  }
}
