package cn.huava.cloud.admin.constant;

/**
 * 关于 @SuppressWarnings("java:S1214"): <br>
 * 当接口只包含常量定义而不包含其他成员时，这个规则就会触发警告。但是当前那只是一个常量类，不需要有其他成员，因此这个警告可以忽略。
 *
 * @author Camio1945
 */
@SuppressWarnings("java:S1214")
public interface AdminConstant {

  int MIN_PASSWORD_LENGTH = 8;

  int MAX_PASSWORD_LENGTH = 20;

}
