package cn.huava.cloud.feign;

import cn.huava.cloud.feign.config.FeignClientConfig;
import cn.huava.cloud.order.pojo.dto.GoodsDto;
import cn.huava.cloud.order.pojo.qo.BuyGoodsQo;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author Camio1945
 */
@FeignClient(name = "goods-service", configuration = FeignClientConfig.class)
public interface GoodsFeignClient {

  /**
   * 根据商品ID获取商品信息
   *
   * @param id 商品ID
   * @return 商品信息
   */
  @GetMapping("/mbr/goods/free/get/{id}")
  GoodsDto getGoodsById(@PathVariable("id") Long id);

  /**
   * 购买商品
   *
   * @param buyGoodsQo 购买商品参数
   */
  @PostMapping("/mbr/goods/auth/buyGoods")
  void buyGoods(@NonNull BuyGoodsQo buyGoodsQo);

  /**
   * 购买商品（使用全局事务，为了方便测试，该接口有一半的概率会抛异常）<br>
   *
   * @param buyGoodsQo 购买商品参数
   */
  @PostMapping("/mbr/goods/auth/buyGoodsWithGlobalTransactionService")
  void buyGoodsWithGlobalTransactionService(@NonNull BuyGoodsQo buyGoodsQo);
}
