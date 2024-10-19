package cn.huava.cloud.goods.job;

import cn.huava.cloud.common.util.RedisUtil;
import cn.huava.cloud.goods.pojo.po.VisitLogPo;
import cn.huava.cloud.goods.service.visitlog.AceVisitLogService;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.springframework.stereotype.Service;

import static cn.huava.cloud.goods.constant.GoodsConstant.CacheConstant.VISIT_LOG_LIST;

/**
 * 定时任务，用于同步商品访问记录，由 xxl-job 调用
 *
 * @author Camio1945
 */
@SuppressWarnings("unused")
@Slf4j
@Service
@RequiredArgsConstructor
public class SyncVisitLogJob {
  private final AceVisitLogService visitLogService;

  @XxlJob("syncVisitLogJobHandler")
  public void handle() {
    RList<Object> rList = RedisUtil.getRedissonClient().getList(VISIT_LOG_LIST);
    int toIndex = 99;
    List<Object> objectList = rList.range(0, toIndex);
    if (objectList.size() == 0) {
      return;
    }
    List<VisitLogPo> logs = objectList.stream().map(o -> (VisitLogPo) o).toList();
    visitLogService.saveBatch(logs);
    rList.trim(toIndex + 1, -1);
    log.info("同步商品访问记录成功，本次共同步 {} 条数据", objectList.size());
  }
}
