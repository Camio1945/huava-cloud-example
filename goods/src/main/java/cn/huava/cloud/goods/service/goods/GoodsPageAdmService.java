package cn.huava.cloud.goods.service.goods;

import cn.huava.cloud.common.pojo.dto.PageDto;
import cn.huava.cloud.common.pojo.qo.OrderByField;
import cn.huava.cloud.common.pojo.qo.PageQo;
import cn.huava.cloud.common.service.BaseService;
import cn.huava.cloud.goods.mapper.GoodsMapper;
import cn.huava.cloud.goods.pojo.po.GoodsPo;
import cn.huava.cloud.goods.util.Fn;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import java.util.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * 商品查询（查 MySQL）<br>
 *
 * @author Camio1945
 */
@Slf4j
@Service
@RequiredArgsConstructor
class GoodsPageAdmService extends BaseService<GoodsMapper, GoodsPo> {

  protected PageDto<GoodsPo> goodsPage4Adm(
      @NonNull PageQo<GoodsPo> pageQo, @NonNull GoodsPo params) {
    LambdaQueryWrapper<GoodsPo> wrapper = Fn.undeletedWrapper(GoodsPo::getDeleteInfo);
    setSelectionByCondition(params, wrapper);
    setOrderByFields(pageQo, wrapper);
    pageQo = page(pageQo, wrapper);
    return new PageDto<>(pageQo.getRecords(), pageQo.getTotal());
  }

  private static void setSelectionByCondition(
      @NotNull GoodsPo params, LambdaQueryWrapper<GoodsPo> wrapper) {
    wrapper
        .like(Fn.isNotBlank(params.getName()), GoodsPo::getName, params.getName())
        .eq(params.getIsOn() != null, GoodsPo::getIsOn, params.getIsOn())
        // 故意去掉了详情字段，以节省网络传输量
        .select(
            GoodsPo::getId,
            GoodsPo::getName,
            GoodsPo::getPrice,
            GoodsPo::getStock,
            GoodsPo::getSales,
            GoodsPo::getIsOn,
            GoodsPo::getImgs,
            GoodsPo::getCreateTime,
            GoodsPo::getUpdateTime);
  }

  /** 设置排序字段 */
  private static void setOrderByFields(
      @NotNull PageQo<GoodsPo> pageQo, LambdaQueryWrapper<GoodsPo> wrapper) {
    Map<String, SFunction<GoodsPo, ?>> fieldToFunctionMap = HashMap.newHashMap(5);
    fieldToFunctionMap.put("id", GoodsPo::getId);
    fieldToFunctionMap.put("price", GoodsPo::getPrice);
    fieldToFunctionMap.put("stock", GoodsPo::getStock);
    fieldToFunctionMap.put("sales", GoodsPo::getSales);
    fieldToFunctionMap.put("createTime", GoodsPo::getCreateTime);
    List<OrderByField<GoodsPo>> orderByFields = pageQo.getOrderByFields(fieldToFunctionMap);
    for (OrderByField<GoodsPo> orderByField : orderByFields) {
      wrapper.orderBy(true, orderByField.isAsc(), orderByField.getField());
    }
  }
}
