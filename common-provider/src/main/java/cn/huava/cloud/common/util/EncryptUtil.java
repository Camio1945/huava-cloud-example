package cn.huava.cloud.common.util;

import lombok.NonNull;
import org.dromara.hutool.extra.spring.SpringUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Camio1945
 */
@SuppressWarnings("unused")
public class EncryptUtil {
  private EncryptUtil() {}

  public static String encryptPassword(@NonNull final String str) {
    PasswordEncoder encoder = SpringUtil.getBean(PasswordEncoder.class);
    return encoder.encode(str);
  }
}
