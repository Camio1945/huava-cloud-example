package cn.huava.cloud.order.service.order;


import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.order.mapper.OrderMapper;
import cn.huava.cloud.order.pojo.po.OrderExtPo;
import cn.huava.cloud.order.pojo.po.OrderPo;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单服务主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AceOrderService extends BaseService<OrderMapper, OrderPo> {
  private final SubmitOrderService submitOrderService;
  private final SubmitOrderWithGlobalTransactionService submitOrderWithGlobalTransactionService;
  private final PayByBalanceService payByBalanceService;
  private final CancelExpiredOrderService cancelExpiredOrderService;

  @Transactional(rollbackFor = Exception.class)
  public Long submitOrder(@NonNull final OrderExtPo orderExtPo) {
    return submitOrderService.submitOrder(orderExtPo);
  }

  /** 这个注解是可有可无的，不加这个注解，出了异常也会回滚，但是推荐加一下，能提升代码的可读性 */
  @GlobalTransactional(rollbackFor = Exception.class)
  public Long submitOrderWithGlobalTransaction(@NonNull final OrderExtPo orderExtPo) {
    return submitOrderWithGlobalTransactionService.submitOrder(orderExtPo);
  }

  public void payByBalance(@NonNull final Long orderId) {
    payByBalanceService.payByBalance(orderId);
  }

  /** 如果超时未支付，则取消订单 */
  public void cancelExpiredOrder() {
    cancelExpiredOrderService.cancelExpiredOrder();
  }
}
