package cn.huava.gateway.constant;

/**
 * 关于 @SuppressWarnings("java:S1214"): <br>
 * 当接口只包含常量定义而不包含其他成员时，这个规则就会触发警告。但是当前那只是一个常量类，不需要有其他成员，因此这个警告可以忽略。
 *
 * @author Camio1945
 */
@SuppressWarnings("java:S1214")
public interface GatewayConstant {

  /** 生产环境 */
  String ENV_PROD = "prod";

  /** 生产环境 */
  String ENV_PRODUCTION = "production";
}
