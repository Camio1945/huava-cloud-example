package cn.huava.cloud.goods.controller;

import cn.huava.cloud.common.controller.BaseController;
import cn.huava.cloud.common.pojo.dto.PageDto;
import cn.huava.cloud.common.pojo.qo.PageQo;
import cn.huava.cloud.common.util.RedisUtil;
import cn.huava.cloud.goods.mapper.GoodsMapper;
import cn.huava.cloud.goods.pojo.po.GoodsPo;
import cn.huava.cloud.goods.service.goods.AceGoodsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 商品控制器（给平台管理员用户提供的接口）
 *
 * @author Camio1945
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/adm/goods")
public class GoodsAdmController extends BaseController<AceGoodsService, GoodsMapper, GoodsPo> {

  @GetMapping("/auth/flushRedis")
  public ResponseEntity<Boolean> flushRedis() {
    RedisUtil.flushNonProductionDb();
    return ResponseEntity.ok(true);
  }

  @PostMapping("/auth/replenishStock")
  public ResponseEntity<Void> replenishStock(@RequestBody @NonNull Long id) {
    service.replenishStock(id);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/auth/page")
  public ResponseEntity<PageDto<GoodsPo>> page(
      @NonNull final PageQo<GoodsPo> pageQo, @NonNull final GoodsPo params) {
    params.setIsOn(true);
    PageDto<GoodsPo> pageDto = service.goodsPage4Adm(pageQo, params);
    return ResponseEntity.ok(pageDto);
  }

  @Override
  protected void afterSave(@NonNull GoodsPo goods) {
    replenishStock(goods.getId());
    service.saveToEs(goods);
  }
}
