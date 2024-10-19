package cn.huava.cloud.member.pojo.po;


import lombok.Data;

/**
 * 注册
 *
 * @author Camio1945
 */
@Data
public class RegisterPo extends MemberPo {
  /** 图形验证码 */
  private String captchaCode;

  /** 是否禁用了验证码以方便测试（该参数仅在非生产环境下有效） */
  private Boolean isCaptchaDisabledForTesting;
}
