package cn.huava.cloud.order.service.order;

import cn.huava.cloud.common.pojo.po.BasePo;
import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.feign.GoodsFeignClient;
import cn.huava.cloud.order.constant.OrderConstant;
import cn.huava.cloud.order.mapper.OrderMapper;
import cn.huava.cloud.order.mq.ReplenishStockMsg;
import cn.huava.cloud.order.pojo.dto.GoodsDto;
import cn.huava.cloud.order.pojo.po.*;
import cn.huava.cloud.order.pojo.qo.BuyGoodsQo;
import cn.huava.cloud.order.service.orderitem.AceOrderItemService;
import cn.huava.cloud.order.util.Fn;
import java.math.BigDecimal;
import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.data.id.IdUtil;
import org.dromara.hutool.core.date.DateUtil;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 订单提交服务<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class SubmitOrderService extends BaseService<OrderMapper, OrderPo> {
  private final GoodsFeignClient goodsFeignClient;
  private final AceOrderItemService orderItemService;
  private final StreamBridge streamBridge;

  protected Long submitOrder(@NonNull final OrderExtPo orderExtPo) {
    BasePo.beforeCreate(orderExtPo);
    orderExtPo.setSn(generateSn());
    orderExtPo.setMemberId(Fn.getLoginMemberId());
    List<OrderItemPo> orderItems = getOrderItems(orderExtPo);
    orderExtPo.setTotalAmount(getTotalAmount(orderItems));
    orderExtPo.setStatus(OrderConstant.OrderStatus.UNPAID);
    save(orderExtPo);
    orderItems.forEach(orderItem -> orderItem.setOrderId(orderExtPo.getId()));
    orderItemService.saveBatch(orderItems);
    buyGoods(orderItems);
    return orderExtPo.getId();
  }

  /** 生成订单编号 */
  protected String generateSn() {
    String time = DateUtil.format(new Date(), "yyMMddHHmmssSSS");
    int amendSize = 5;
    return time + IdUtil.nanoId(amendSize);
  }

  private static BigDecimal getTotalAmount(List<OrderItemPo> orderItems) {
    BigDecimal totalAmount = new BigDecimal(0);
    for (OrderItemPo orderItem : orderItems) {
      totalAmount = totalAmount.add(orderItem.getTotalAmount());
    }
    return totalAmount;
  }

  /** 购买商品 */
  private void buyGoods(List<OrderItemPo> orderItems) {
    for (OrderItemPo orderItem : orderItems) {
      BuyGoodsQo decreaseStockQo =
          new BuyGoodsQo()
              .setGoodsId(orderItem.getGoodsId())
              .setGoodsCount(orderItem.getGoodsCount());
      goodsFeignClient.buyGoods(decreaseStockQo);
    }
  }

  private List<OrderItemPo> getOrderItems(OrderExtPo orderExtPo) {
    List<OrderItemPo> orderItems = orderExtPo.getOrderItems();
    Assert.notEmpty(orderItems, "订单商品不能为空");
    for (OrderItemPo orderItem : orderItems) {
      GoodsDto goodsPo = getGoodsPo(orderItem);
      orderItem.setId(null);
      orderItem.setGoodsImg(getFirstImg(goodsPo));
      orderItem.setGoodsPrice(goodsPo.getPrice());
      orderItem.setTotalAmount(
          orderItem.getGoodsPrice().multiply(new BigDecimal(orderItem.getGoodsCount())));
    }
    return orderItems;
  }

  private String getFirstImg(GoodsDto goodsPo) {
    String imgs = goodsPo.getImgs();
    if (Fn.isNotBlank(imgs)) {
      return imgs.split(",")[0];
    }
    return null;
  }

  private GoodsDto getGoodsPo(OrderItemPo orderItem) {
    Long goodsId = orderItem.getGoodsId();
    Assert.notNull(goodsId, "商品ID不能为空");
    Assert.isTrue(orderItem.getGoodsCount() > 0, "商品数量必须大于0");
    GoodsDto goodsPo = goodsFeignClient.getGoodsById(goodsId);
    Assert.notNull(goodsPo, "商品不存在");
    Assert.isTrue(goodsPo.getIsOn(), "商品已下架");
    sendReplenishStockMqMsgIfNeeded(orderItem, goodsPo);
    Assert.isTrue(goodsPo.getStock() >= orderItem.getGoodsCount(), "商品库存不足");
    return goodsPo;
  }

  private void sendReplenishStockMqMsgIfNeeded(OrderItemPo orderItem, GoodsDto goodsPo) {
    int stock = goodsPo.getStock();
    int minStock = 100;
    // 如果库存大于订单数量，且库存大于100，则不需要发消息
    if (stock >= orderItem.getGoodsCount() && stock >= minStock) {
      return;
    }
    Map<String, Object> headers = HashMap.newHashMap(2);
    Long goodsId = goodsPo.getId();
    String key = "order-service-replenish-stock-goods-id-" + goodsId;
    headers.put("subjectId", goodsId);
    ReplenishStockMsg replenishStockMsg = new ReplenishStockMsg().setGoodsId(goodsId);
    Message<ReplenishStockMsg> msg = new GenericMessage<>(replenishStockMsg, headers);
    streamBridge.send("replenishStockProducer-out-0", msg);
  }
}
