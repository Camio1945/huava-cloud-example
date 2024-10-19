package cn.huava.cloud.goods.constant;

/**
 * 商品常量
 *
 * @author Camio1945
 */
public interface GoodsConstant {

  /** 缓存常量 */
  interface CacheConstant {
    String COMMON_PREFIX = "goods-service:cache:";

    /** 商品访问记录列表 */
    String VISIT_LOG_LIST = COMMON_PREFIX + "visitLogList:";
  }
}
