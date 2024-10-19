package cn.huava.cloud.goods.controller;

import cn.huava.cloud.common.pojo.qo.PageQo;
import cn.huava.cloud.goods.pojo.doc.GoodsDoc;
import cn.huava.cloud.goods.pojo.po.GoodsPo;
import cn.huava.cloud.goods.pojo.qo.BuyGoodsQo;
import cn.huava.cloud.goods.pojo.qo.SearchGoodsQo;
import cn.huava.cloud.goods.service.goods.AceGoodsService;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 商品控制器
 *
 * @author Camio1945
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mbr/goods")
public class GoodsMbrController {
  private final AceGoodsService service;

  @GetMapping("/free/page")
  public ResponseEntity<List<GoodsDoc>> page(
      @NonNull final PageQo<GoodsPo> pageQo, @NonNull final SearchGoodsQo params) {
    return ResponseEntity.ok(service.goodsPage4Mbr(pageQo, params));
  }

  @GetMapping("/free/get/{id}")
  public ResponseEntity<GoodsPo> getByIdNoAuth(@PathVariable @NonNull final Long id) {
    service.sendVisitLogToRedis(id);
    return ResponseEntity.ok(service.getById(id));
  }

  @PostMapping("/auth/buyGoods")
  public ResponseEntity<Void> buyGoods(@RequestBody @NonNull BuyGoodsQo buyGoodsQo) {
    service.buyGoods(buyGoodsQo);
    return ResponseEntity.ok(null);
  }

  /** 购买商品（使用全局事务，为了方便测试，该接口有一半的概率会抛异常） */
  @PostMapping("/auth/buyGoodsWithGlobalTransactionService")
  public ResponseEntity<Void> buyGoodsWithGlobalTransactionService(
      @RequestBody @NonNull BuyGoodsQo buyGoodsQo) {
    service.buyGoodsWithGlobalTransactionService(buyGoodsQo);
    return ResponseEntity.ok(null);
  }
}
