package cn.huava.cloud.common.constant;

/**
 * 关于 @SuppressWarnings("java:S1214"): <br>
 * 当接口只包含常量定义而不包含其他成员时，这个规则就会触发警告。但是当前那只是一个常量类，不需要有其他成员，因此这个警告可以忽略。
 *
 * @author Camio1945
 */
@SuppressWarnings({"java:S1214", "unused"})
public interface CommonConstant {
  /** 一秒有多少毫秒 */
  long MILLIS_PER_SECOND = 1000L;

  /** 验证码在 Session 中的 key */
  String CAPTCHA_CODE_SESSION_KEY = "captchaCode";

  /** 生产环境 */
  String ENV_PROD = "prod";

  /** 生产环境 */
  String ENV_PRODUCTION = "production";

  /** 认证请求头信息，对应的值就是 access token */
  String AUTHORIZATION_HEADER = "Authorization";

  /** 分页查询时最大允许每页多少条数据 */
  int MAX_PAGE_SIZE = 500;

  /** 超级管理员角色 id ，规范定死的，不要改 */
  long ADMIN_ROLE_ID = 1L;

  /** 超级管理员用户 id ，规范定死的，不要改 */
  long ADMIN_USER_ID = 1L;

  /** 上传文件时，表单中文件参数的名称 */
  String MULTIPART_PARAM_NAME = "file";

  /** JWT 令牌的前缀 */
  String BEARER_PREFIX = "Bearer ";

  /** Trace ID key，用于追踪 ID */
  String TRACE_ID_KEY = "tid";

  /** Request ID key，用于请求 ID */
  String REQUEST_ID_KEY = "rid";

  /** Subject ID key，一般是后台用户 ID，或前台用户 ID */
  String SUBJECT_ID_KEY = "subjectId";

  /** 请求是否来自于内网 */
  String IS_REQUEST_FROM_INNER_NET_KEY = "isRequestFromInnerNet";


}
