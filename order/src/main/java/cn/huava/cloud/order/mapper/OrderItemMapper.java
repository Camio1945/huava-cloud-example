package cn.huava.cloud.order.mapper;

import cn.huava.cloud.order.pojo.po.OrderItemPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import org.apache.ibatis.annotations.*;

/**
 * 订单项数据库操作
 *
 * @author Camio1945
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemPo> {

  /**
   * 批量插入
   *
   * @param orderItems 订单项列表
   */
  @Insert(
      """
        <script>
          INSERT INTO `order_item` (`order_id`, `goods_id`, `goods_count`, `goods_price`, `goods_img`, `total_amount`)
          VALUES
          <foreach collection="orderItems" item="item" separator="),(" open="(" close=")">
            #{item.orderId}, #{item.goodsId}, #{item.goodsCount}, #{item.goodsPrice}, #{item.goodsImg}, #{item.totalAmount}
          </foreach>
        </script>
      """)
  void insertBatch(List<OrderItem> orderItems);

  /**
   * 查询商品总数
   *
   * @return 商品总数
   */
  @Select("SELECT IFNULL(SUM(`goods_count`),0) FROM `order_item`")
  int selectTotalGoodsCount();
}
