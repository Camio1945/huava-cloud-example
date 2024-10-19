package cn.huava.cloud.goods.service.goods;

import cn.huava.cloud.common.pojo.dto.PageDto;
import cn.huava.cloud.common.pojo.qo.PageQo;
import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.goods.mapper.GoodsMapper;
import cn.huava.cloud.goods.pojo.doc.GoodsDoc;
import cn.huava.cloud.goods.pojo.po.GoodsPo;
import cn.huava.cloud.goods.pojo.po.ReplenishLogPo;
import cn.huava.cloud.goods.pojo.qo.BuyGoodsQo;
import cn.huava.cloud.goods.pojo.qo.SearchGoodsQo;
import cn.huava.cloud.goods.repository.GoodsRepository;
import cn.huava.cloud.goods.service.replenishlog.AceReplenishLogService;
import cn.huava.cloud.goods.util.Fn;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import java.util.Date;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 商品服务主入口类<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AceGoodsService extends BaseService<GoodsMapper, GoodsPo> {
  private final GoodsPageAdmService goodsPageAdmService;
  private final GoodsPageMbrService goodsPageMbrService;
  private final SendVisitLogToRedisService sendVisitLogToRedisService;
  private final AceReplenishLogService replenishLogService;
  private final GoodsRepository goodsRepository;

  public PageDto<GoodsPo> goodsPage4Adm(@NonNull PageQo<GoodsPo> pageQo, @NonNull GoodsPo params) {
    return goodsPageAdmService.goodsPage4Adm(pageQo, params);
  }

  public List<GoodsDoc> goodsPage4Mbr(
      @NonNull PageQo<GoodsPo> pageQo, @NonNull SearchGoodsQo params) {
    return goodsPageMbrService.goodsPage4Mbr(pageQo, params);
  }

  public void sendVisitLogToRedis(@NonNull Long goodsId) {
    sendVisitLogToRedisService.sendVisitLogToRedis(goodsId);
  }

  /**
   * 更新库存（增加或减少指定的数量，而不把库存设置成固定值）
   *
   * @param id 商品ID
   * @param delta 增加或减少的数量。如果是正数，则表示增加库存，如果是负数，则表示减少库存。
   */
  public void updateStockByDelta(@NonNull Long id, int delta) {
    if (delta == 0) {
      return;
    }
    LambdaUpdateWrapper<GoodsPo> rapper =
        new LambdaUpdateWrapper<GoodsPo>()
            .eq(GoodsPo::getId, id)
            .setIncrBy(GoodsPo::getStock, delta);
    update(rapper);
  }

  @Transactional(rollbackFor = Exception.class)
  public void replenishStock(@NonNull Long id) {
    int stock = 100;
    updateStockByDelta(id, stock);
    ReplenishLogPo replenishLogPo = new ReplenishLogPo();
    replenishLogPo.setGoodsId(id);
    replenishLogPo.setStock(stock);
    replenishLogPo.setCreateTime(new Date());
    replenishLogService.save(replenishLogPo);
  }

  public void buyGoods(@NonNull BuyGoodsQo buyGoodsQo) {
    int goodsCount = buyGoodsQo.getGoodsCount();
    Assert.isTrue(goodsCount > 0, "商品数量必须大于0");
    LambdaUpdateWrapper<GoodsPo> rapper =
        new LambdaUpdateWrapper<GoodsPo>()
            .eq(GoodsPo::getId, buyGoodsQo.getGoodsId())
            .setDecrBy(GoodsPo::getStock, goodsCount)
            .setIncrBy(GoodsPo::getSales, buyGoodsQo.getGoodsCount());
    update(rapper);
  }

  public void buyGoodsWithGlobalTransactionService(@NonNull BuyGoodsQo buyGoodsQo) {
    simulateException();
    int goodsCount = buyGoodsQo.getGoodsCount();
    Assert.isTrue(goodsCount > 0, "商品数量必须大于0");
    LambdaUpdateWrapper<GoodsPo> rapper =
        new LambdaUpdateWrapper<GoodsPo>()
            .eq(GoodsPo::getId, buyGoodsQo.getGoodsId())
            .setDecrBy(GoodsPo::getStock, goodsCount)
            .setIncrBy(GoodsPo::getSales, buyGoodsQo.getGoodsCount());
    update(rapper);
  }

  public void saveToEs(@NonNull GoodsPo goods) {
    GoodsDoc goodsDoc = Fn.toBean(goods, GoodsDoc.class);
    goodsRepository.save(goodsDoc);
  }

  private static void simulateException() {
    ExceptionUtil.wrapRuntimeAndThrow("为测试分布式事务而模拟的异常");
    int limitExclude = 2;
    if (RandomUtil.randomInt(limitExclude) == 0) {
      ExceptionUtil.wrapRuntimeAndThrow("为测试分布式事务而模拟的异常");
    }
  }
}
