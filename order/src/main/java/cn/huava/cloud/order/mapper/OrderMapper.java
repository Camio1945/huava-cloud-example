package cn.huava.cloud.order.mapper;

import cn.huava.cloud.order.pojo.po.OrderPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单数据库操作
 *
 * @author Camio1945
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderPo> {}
