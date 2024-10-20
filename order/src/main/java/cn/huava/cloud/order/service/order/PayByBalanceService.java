package cn.huava.cloud.order.service.order;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.feign.MemberFeignClient;
import cn.huava.cloud.member.pojo.po.MemberPo;
import cn.huava.cloud.order.constant.OrderConstant;
import cn.huava.cloud.order.mapper.OrderMapper;
import cn.huava.cloud.order.mq.ReplenishBalanceMsg;
import cn.huava.cloud.order.mq.UpdateMemberPointMsg;
import cn.huava.cloud.order.pojo.po.OrderPo;
import cn.huava.cloud.order.pojo.qo.DecreaseBalanceQo;
import cn.huava.cloud.order.util.Fn;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import java.math.BigDecimal;
import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.dromara.hutool.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 支付<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class PayByBalanceService extends BaseService<OrderMapper, OrderPo> {
  private final MemberFeignClient memberFeignClient;
  private final StreamBridge streamBridge;

  public void payByBalance(@NonNull Long orderId) {
    OrderPo orderPo = getAndCheckOrder(orderId);
    // 减少会员余额
    decreaseBalance(orderPo);
    // 修改订单状态
    updateOrderStatus(orderId);
    // 增加用户积分（用消息队列实现）
    sendIncreasePointMqMsg(orderPo);
  }

  @NotNull
  private static UpdateMemberPointMsg getUpdateMemberPointMsg(OrderPo orderPo) {
    UpdateMemberPointMsg updateMemberPointMsg = new UpdateMemberPointMsg();
    updateMemberPointMsg.setMemberId(orderPo.getMemberId());
    updateMemberPointMsg.setPoint(orderPo.getTotalAmount().intValue());
    updateMemberPointMsg.setRemark("订单支付");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("orderId", orderPo.getId());
    updateMemberPointMsg.setDetail(jsonObject.toString());
    return updateMemberPointMsg;
  }

  private void sendIncreasePointMqMsg(final OrderPo orderPo) {
    Map<String, Object> headers = HashMap.newHashMap(2);
    String key = "order-service-increase-point-order-id-" + orderPo.getId();
    headers.put("subjectId", orderPo.getMemberId());
    headers.put(MessageConst.PROPERTY_KEYS, key);
    headers.put(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, key);
    UpdateMemberPointMsg updateMemberPointMsg = getUpdateMemberPointMsg(orderPo);
    Message<UpdateMemberPointMsg> msg = new GenericMessage<>(updateMemberPointMsg, headers);
    streamBridge.send("updateMemberPointProducer-out-0", msg);
  }

  private void updateOrderStatus(@NotNull Long orderId) {
    LambdaUpdateWrapper<OrderPo> wrapper =
        new LambdaUpdateWrapper<OrderPo>()
            .eq(OrderPo::getId, orderId)
            .set(OrderPo::getStatus, OrderConstant.OrderStatus.PAID)
            .set(OrderPo::getUpdateTime, new Date());
    update(wrapper);
  }

  @NotNull
  private OrderPo getAndCheckOrder(@NotNull Long orderId) {
    OrderPo orderPo = getById(orderId);
    MemberPo member = Fn.getLoginMember();
    Assert.isTrue(member.getId().longValue() == orderPo.getMemberId(), "订单与当前用户不匹配");
    int status = orderPo.getStatus();
    Assert.isTrue(status == OrderConstant.OrderStatus.UNPAID, "订单状态异常");
    BigDecimal balance = member.getBalance();
    sendReplenishBalanceMqMsgIfNeeded(balance, orderPo);
    Assert.isTrue(balance.compareTo(orderPo.getTotalAmount()) >= 0, "余额不足");
    return orderPo;
  }

  private void sendReplenishBalanceMqMsgIfNeeded(BigDecimal balance, OrderPo orderPo) {
    int minBalance = 1000;
    if (balance.compareTo(orderPo.getTotalAmount()) < 0
        || balance.compareTo(new BigDecimal(minBalance)) < 0) {
      Map<String, Object> headers = HashMap.newHashMap(2);
      String key =
          Fn.format(
              "order-service-replenish-balance-member-id-{}-order-id-{}",
              orderPo.getMemberId(),
              orderPo.getId());
      headers.put("subjectId", orderPo.getMemberId());
      headers.put(MessageConst.PROPERTY_KEYS, key);
      headers.put(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID, key);
      ReplenishBalanceMsg updateMemberPointMsg = new ReplenishBalanceMsg();
      updateMemberPointMsg.setMemberId(orderPo.getMemberId());
      Message<ReplenishBalanceMsg> msg = new GenericMessage<>(updateMemberPointMsg, headers);
      streamBridge.send("replenishBalanceProducer-out-0", msg);
    }
  }

  /** 减少会员余额 */
  private void decreaseBalance(OrderPo orderPo) {
    DecreaseBalanceQo decreaseBalanceQo = new DecreaseBalanceQo();
    decreaseBalanceQo.setDecreaseAmount(orderPo.getTotalAmount());
    decreaseBalanceQo.setRemark("订单支付");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("orderId", orderPo.getId());
    decreaseBalanceQo.setDetail(jsonObject.toString());
    memberFeignClient.decreaseBalance(decreaseBalanceQo);
  }
}
