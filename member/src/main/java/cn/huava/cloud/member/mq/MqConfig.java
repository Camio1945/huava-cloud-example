package cn.huava.cloud.member.mq;

import java.util.Date;
import java.util.function.Consumer;

import cn.huava.cloud.member.service.member.AceMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

/**
 * 消息队列配置
 *
 * @author Camio1945
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MqConfig {

  private final AceMemberService memberService;

  /**
   * 与 *.yml（可能是 nacos 中的 *.yml ） 文件中的以下配置对应：
   *
   * <pre>
   * spring.cloud.function.definition
   * spring.cloud.stream.rocketmq.bindings.updateMemberPointConsumer-in-0
   * spring.cloud.stream.bindings.updateMemberPointConsumer-in-0
   * </pre>
   *
   * @return
   */
  @Bean
  public Consumer<Message<UpdateMemberPointMsg>> updateMemberPointConsumer() {
    return msg -> {
      UpdateMemberPointMsg updateMemberPointMsg = msg.getPayload();
      log.info("收到消息：{}", updateMemberPointMsg);
      memberService.updateMemberPoint(updateMemberPointMsg);
    };

  }
}
