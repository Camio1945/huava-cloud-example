package cn.huava.cloud.common.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码加密器配置
 *
 * @author Camio1945
 */
@Configuration
@AllArgsConstructor
public class PasswordEncoderConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
