package cn.huava.cloud.goods.job;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.springframework.stereotype.Service;
import com.xxl.job.core.handler.annotation.XxlJob;

import java.util.Date;

/**
 * 定时任务，用于测试，由 xxl-job 调用
 *
 * @author Camio1945
 */
@SuppressWarnings("unused")
@Service
public class TestJob {

  @XxlJob("demoJobHandler")
  public void test() {
    System.out.println("test : " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    ThreadUtil.sleep(4000);
  }
}
