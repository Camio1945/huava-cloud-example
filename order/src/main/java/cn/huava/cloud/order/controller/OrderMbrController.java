package cn.huava.cloud.order.controller;

import cn.huava.cloud.common.validation.Create;
import cn.huava.cloud.order.pojo.po.OrderExtPo;
import cn.huava.cloud.order.service.order.AceOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 *
 * @author Camio1945
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mbr/order")
public class OrderMbrController {
  private final AceOrderService orderService;

  @PostMapping("/auth/submit")
  public ResponseEntity<Long> submit(
      @RequestBody @NonNull @Validated({Create.class}) final OrderExtPo orderExtPo) {
    return ResponseEntity.ok(orderService.submitOrder(orderExtPo));
  }

  @PostMapping("/auth/submitWithGlobalTransaction")
  @GlobalTransactional(rollbackFor = Exception.class)
  public ResponseEntity<Long> submitWithGlobalTransaction(
      @RequestBody @NonNull @Validated({Create.class}) final OrderExtPo orderExtPo) {
    return ResponseEntity.ok(orderService.submitOrderWithGlobalTransaction(orderExtPo));
  }

  @PostMapping("/auth/payByBalance/{orderId}")
  public ResponseEntity<Void> payByBalance(@PathVariable @NonNull Long orderId) {
    orderService.payByBalance(orderId);
    return ResponseEntity.ok(null);
  }
}
