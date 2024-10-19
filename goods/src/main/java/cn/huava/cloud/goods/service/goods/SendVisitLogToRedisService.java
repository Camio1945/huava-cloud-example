package cn.huava.cloud.goods.service.goods;

import static cn.huava.cloud.common.constant.CommonConstant.IS_REQUEST_FROM_INNER_NET_KEY;
import static cn.huava.cloud.goods.constant.GoodsConstant.CacheConstant.VISIT_LOG_LIST;

import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.common.util.RedisUtil;
import cn.huava.cloud.goods.mapper.GoodsMapper;
import cn.huava.cloud.goods.pojo.po.GoodsPo;
import cn.huava.cloud.goods.pojo.po.VisitLogPo;
import cn.huava.cloud.goods.util.Fn;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import java.util.Date;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 发送访问记录给 Redis <br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class SendVisitLogToRedisService extends BaseService<GoodsMapper, GoodsPo> {
  protected void sendVisitLogToRedis(@NonNull Long goodsId) {
    // 如果是内网访问，则不认为是用户发起的访问，不记录
    String isRequestFromInnerNet = Fn.getRequest().getHeader(IS_REQUEST_FROM_INNER_NET_KEY);
    if (StringPool.TRUE.equals(isRequestFromInnerNet)) {
      return;
    }
    Long memberId = getMemberId();
    VisitLogPo log =
        new VisitLogPo().setGoodsId(goodsId).setMemberId(memberId).setCreateTime(new Date());
    RedisUtil.putListValue(VISIT_LOG_LIST, log);
  }

  private static Long getMemberId() {
    Long memberId = 0L;
    try {
      memberId = Fn.getLoginMemberId();
    } catch (Exception e) {
      log.warn("发送访问记录给 Redis 时获取登录用户信息失败，使用默认值 0 ：{}", e.getMessage());
    }
    return memberId;
  }
}
