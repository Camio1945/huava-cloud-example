package cn.huava.cloud.member.mq;

import lombok.Data;

/**
 * 更新用户积分消息
 *
 * @author Camio1945
 */
@Data
public class UpdateMemberPointMsg {
  /** 全员ID */
  private Long memberId;

  /** 积分，正数加积分，负数减积分 */
  private Integer point;

  /** 备注，如：订单支付 */
  private String remark;

  /** 详情，如：{"orderId":1} */
  private String detail;
}
