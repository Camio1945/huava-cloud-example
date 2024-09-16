package cn.huava.gateway;

import cn.huava.gateway.common.graalvm.NativeRuntimeHintsRegistrar;
import cn.huava.gateway.common.graalvm.SerializationConfigGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

/**
 * @author Camio1945
 */
@SpringBootApplication
@ImportRuntimeHints(NativeRuntimeHintsRegistrar.class)
public class GatewayApplication {

  public static void main(String[] args) {
    // 为了避免不必要的麻烦，以下这行代码请不要删除，具体功能见方法本身的注释
    SerializationConfigGenerator.generateSerializationConfigFile();
    SpringApplication.run(GatewayApplication.class, args);
  }
}
