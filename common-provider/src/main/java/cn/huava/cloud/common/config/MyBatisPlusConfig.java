package cn.huava.cloud.common.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.*;

/**
 * MyBatisPlus 配置
 *
 * @author Camio1945
 */
@Configuration
@AllArgsConstructor
public class MyBatisPlusConfig {

  @Bean
  public PaginationInnerInterceptor paginationInnerInterceptor() {
    return new PaginationInnerInterceptor();
  }
}
