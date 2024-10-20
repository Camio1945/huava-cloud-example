package cn.huava.cloud.order.service.order;

import static cn.huava.cloud.order.constant.OrderConstant.MINUTES_BEFORE_CANCEL_UNPAID_ORDER;
import static cn.huava.cloud.order.constant.OrderConstant.OrderStatus.CANCELED;
import static cn.huava.cloud.order.constant.OrderConstant.OrderStatus.UNPAID;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.order.mapper.OrderMapper;
import cn.huava.cloud.order.pojo.po.OrderPo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.springframework.stereotype.Service;

/**
 * 取消<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class CancelOrderService extends BaseService<OrderMapper, OrderPo> {

  protected void cancel(@NonNull Long orderId) {
    OrderPo orderPo = getById(orderId);
    if (orderPo.getStatus() != UNPAID) {
      return;
    }
    DateTime deadline =
        DateUtil.offsetMinute(orderPo.getCreateTime(), MINUTES_BEFORE_CANCEL_UNPAID_ORDER);
    if (orderPo.getCreateTime().before(deadline)) {
      return;
    }
    orderPo.setStatus(CANCELED);
    LambdaUpdateWrapper<OrderPo> wrapper = new LambdaUpdateWrapper<>();
    wrapper.set(OrderPo::getStatus, orderPo.getStatus()).eq(OrderPo::getId, orderId);
    update(wrapper);
  }
}
