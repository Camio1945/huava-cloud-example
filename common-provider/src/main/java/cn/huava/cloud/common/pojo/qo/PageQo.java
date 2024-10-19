package cn.huava.cloud.common.pojo.qo;

import static cn.huava.cloud.common.constant.CommonConstant.MAX_PAGE_SIZE;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.*;
import lombok.Data;
import lombok.NonNull;
import org.dromara.hutool.core.lang.Assert;

/**
 * 分页查询对象
 *
 * @author Camio1945
 */
@Data
@SuppressWarnings("unused")
public class PageQo<T> extends Page<T> {
  /**
   * 排序，取值示例 <br>
   * `id`：按 id 升序 <br>
   * `name,id`：先按 name 升序，再按 id 升序 <br>
   * `-createTime,id`：先按 createTime 降序，再按 id 升序 <br>
   */
  private String orderBy;

  @Override
  public Page<T> setSize(long size) {
    Assert.isTrue(size > 0 && size <= MAX_PAGE_SIZE, "每页条数必须大于 0 且小于等于 " + MAX_PAGE_SIZE);
    return super.setSize(size);
  }

  @Override
  public Page<T> setCurrent(long current) {
    Assert.isTrue(current > 0, "当前页数必须大于 0");
    return super.setCurrent(current);
  }

  /** 排序字段列表 */
  public @NonNull List<OrderByField<T>> getOrderByFields(
      Map<String, SFunction<T, ?>> fieldToFunctionMap) {
    List<OrderByField<T>> orderByFields = new ArrayList<>();
    if (orderBy != null && !orderBy.isEmpty()) {
      String[] orderByArray = orderBy.replace("'", "").replace(" ", "").split(",");
      for (String orderByItem : orderByArray) {
        boolean isAsc = !orderByItem.startsWith("-");
        String fieldName = orderByItem.replace("-", "");
        SFunction<T, ?> function = fieldToFunctionMap.get(fieldName);
        orderByFields.add(new OrderByField().setAsc(isAsc).setField(function));
      }
    }
    return orderByFields;
  }
}
