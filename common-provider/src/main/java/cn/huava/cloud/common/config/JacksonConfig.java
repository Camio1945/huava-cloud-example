package cn.huava.cloud.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 解决前后端交互时 Long 类型精度丢失的问题
 *
 * @author Camio1945
 */
@Configuration
public class JacksonConfig {
  @Bean
  public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
    ObjectMapper objectMapper = builder.createXmlMapper(false).build();
    // json Long --> String
    objectMapper.registerModule(
        new SimpleModule().addSerializer(Long.class, ToStringSerializer.instance));
    return objectMapper;
  }
}
