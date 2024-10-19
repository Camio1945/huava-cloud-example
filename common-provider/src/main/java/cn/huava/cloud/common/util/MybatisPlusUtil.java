package cn.huava.cloud.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.NonNull;
import org.dromara.hutool.core.lang.Assert;

/**
 * @author Camio1945
 */
@SuppressWarnings("unused")
public class MybatisPlusUtil {
  private MybatisPlusUtil() {}

  public static <T> LambdaQueryWrapper<T> buildUndeletedWrapper(
      @NonNull final SFunction<T, ?> deleteInfoColumn) {
    Assert.equals("getDeleteInfo", LambdaUtils.extract(deleteInfoColumn).getImplMethodName());
    return new LambdaQueryWrapper<T>().eq(deleteInfoColumn, 0);
  }
}
