package temp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.huava.cloud.goods.service.goods.AceGoodsService;
import cn.huava.cloud.goods.util.Fn;
import cn.huava.common.WithSpringBootTestAnnotation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/** 临时测试类 */
@Slf4j
class TempTest extends WithSpringBootTestAnnotation {
  private static AceGoodsService goodsService;
  private static List<Thread> threadList = new ArrayList<>();
  private static Long goodsId = 1L;

  @Test
  void tempTest() throws Exception {
    goodsService = Fn.getBean(AceGoodsService.class);
    int stockBefore = goodsService.getById(goodsId).getStock();
    minus();
    add();
    joinThreads(threadList);
    int stockAfter = goodsService.getById(goodsId).getStock();
    log.info("stockBefore: {}, stockAfter: {}", stockBefore, stockAfter);
    assertEquals(stockBefore, stockAfter);
  }

  private void add() {
    int threadCount = 200;
    CountDownLatch countDownLatch = new CountDownLatch(threadCount);
    for (int i = 0; i < threadCount; i++) {
      Thread thread =
          Thread.startVirtualThread(
              () -> {
                awaitCountDownLatch(countDownLatch);
                goodsService.updateStockByDelta(goodsId, 100);
              });
      threadList.add(thread);
      countDownLatch.countDown();
    }
  }

  private void minus() {
    int threadCount = 2000;
    CountDownLatch countDownLatch = new CountDownLatch(threadCount);
    for (int i = 0; i < threadCount; i++) {
      Thread thread =
          Thread.startVirtualThread(
              () -> {
                awaitCountDownLatch(countDownLatch);
                goodsService.updateStockByDelta(goodsId, -10);
              });
      threadList.add(thread);
      countDownLatch.countDown();
    }
  }

  private void awaitCountDownLatch(CountDownLatch countDownLatch) {
    try {
      countDownLatch.await();
    } catch (InterruptedException e) {
      log.error("InterruptedException", e);
    }
  }

  private void joinThreads(List<Thread> threadList) throws InterruptedException {
    for (Thread thread : threadList) {
      thread.join();
    }
  }
}
