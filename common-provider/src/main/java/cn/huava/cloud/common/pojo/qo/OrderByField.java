package cn.huava.cloud.common.pojo.qo;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Data;

/**
 * 排序字段
 *
 * @author Camio1945
 */
@Data
public class OrderByField<T> {
  /** 是否为升序 */
  private boolean isAsc;

  /** 字段，是实体类的某个 getter 方法 */
  private SFunction<T, ?> field;
}
