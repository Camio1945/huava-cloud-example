package cn.huava.cloud.order.job;


import cn.huava.cloud.order.service.order.AceOrderService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 定时任务，由 xxl-job 调用其中用 @XxlJob 注解了的方法
 *
 * @author Camio1945
 */
@SuppressWarnings("unused")
@Slf4j
@Service
@RequiredArgsConstructor
public class XllJob {
  private final AceOrderService orderService;

  @XxlJob("cancelExpiredOrderJobHandler")
  public void handle() {
    log.info("取消过期的订单");
    orderService.cancelExpiredOrder();
  }

  @XxlJob("demoJobHandler")
  public void test() {
    System.out.println("test : " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    ThreadUtil.sleep(4000);
  }
}
