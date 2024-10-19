package cn.huava.cloud.common.validation;

import cn.huava.cloud.common.pojo.po.BasePo;
import cn.huava.cloud.common.util.HttpServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.dromara.hutool.core.lang.Assert;

/**
 * 基础验证器，用于被其他校验器继承
 *
 * @author Camio1945
 */
@SuppressWarnings("unused")
public class BaseValidator {

  public static boolean basicValidate(BasePo basePo) {
    HttpServletRequest request = HttpServletUtil.getRequest();
    boolean isCreate = request.getRequestURI().endsWith("/register");
    boolean isUpdate = request.getRequestURI().endsWith("/update");
    Assert.isTrue(isCreate || isUpdate, "目前仅允许在执行创建或更新操作时验证唯一性");
    if (isUpdate) {
      Assert.notNull(basePo.getId(), "执行更新操作时，ID不能为空");
    }
    return isUpdate;
  }

  public static boolean customMessage(ConstraintValidatorContext context, String messageTemplate) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
    return false;
  }
}
