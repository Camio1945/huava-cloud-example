package cn.huava.cloud;

import cn.huava.cloud.common.annotation.UnreachableForTesting;
import java.io.Serializable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动器
 *
 * @author Camio1945
 */
@UnreachableForTesting("启动类在单元测试时用不到。 Spring 在单元测试时好像另有一套启动机制。")
@SpringBootApplication
public class MemberApplication implements Serializable {

  public static void main(String[] args) {
    SpringApplication.run(MemberApplication.class, args);
  }
}
