package cn.huava.cloud.order.constant;


/**
 * 订单模块的常量
 *
 * @author Camio1945
 */
public interface OrderConstant {

  /** 取消未支付的订单前等待多少分钟 */
  int MINUTES_BEFORE_CANCEL_UNPAID_ORDER = 10;

  /** 订单状态 */
  interface OrderStatus {
    /** 状态：1-待支付，2-待发货（已支付），3-待收货（已发货），4-已完成，5-已取消，6-已退款 */
    int UNPAID = 1;

    int PAID = 2;
    int SHIPPED = 3;
    int COMPLETED = 4;
    int CANCELED = 5;
    int REFUNDED = 6;
  }
}
