package cn.huava.cloud;

import java.io.Serializable;

import cn.huava.cloud.common.annotation.UnreachableForTesting;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动器
 *
 * @author Camio1945
 */
@UnreachableForTesting("启动类在单元测试时用不到。 Spring 在单元测试时好像另有一套启动机制。")
@SpringBootApplication
@EnableFeignClients
public class AdminApplication implements Serializable {

  public static void main(String[] args) {
    SpringApplication.run(AdminApplication.class, args);
  }
}
