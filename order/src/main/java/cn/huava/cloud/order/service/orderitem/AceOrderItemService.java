package cn.huava.cloud.order.service.orderitem;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.order.mapper.OrderItemMapper;
import cn.huava.cloud.order.pojo.po.OrderItemPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 订单项服务主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
public class AceOrderItemService extends BaseService<OrderItemMapper, OrderItemPo> {}
