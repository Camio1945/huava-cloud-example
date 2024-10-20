package cn.huava.cloud.order.service.order;

import static cn.huava.cloud.order.constant.OrderConstant.MINUTES_BEFORE_CANCEL_UNPAID_ORDER;
import static cn.huava.cloud.order.constant.OrderConstant.OrderStatus.CANCELED;
import static cn.huava.cloud.order.constant.OrderConstant.OrderStatus.UNPAID;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.order.mapper.OrderMapper;
import cn.huava.cloud.order.pojo.po.OrderPo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
class CancelExpiredOrderService extends BaseService<OrderMapper, OrderPo> {

  protected void cancelExpiredOrder() {
    LambdaUpdateWrapper<OrderPo> wrapper = new LambdaUpdateWrapper<>();
    DateTime minutesAgo =
        DateUtil.offsetMinute(DateUtil.now(), -MINUTES_BEFORE_CANCEL_UNPAID_ORDER);
    wrapper
        .set(OrderPo::getStatus, CANCELED)
        .eq(OrderPo::getStatus, UNPAID)
        .le(OrderPo::getCreateTime, minutesAgo);
    update(wrapper);
  }
}
