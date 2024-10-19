package cn.huava.cloud;

import cn.huava.cloud.common.annotation.UnreachableForTesting;
import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OrderApplication implements Serializable {

  /***
   * tag array.
   */
  public static final String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};

  public static void main(String[] args) {
    SpringApplication.run(OrderApplication.class, args);
  }

}
