package cn.huava.cloud.admin.validation.user;

import static cn.huava.cloud.common.constant.CommonConstant.ADMIN_USER_ID;

import cn.huava.cloud.admin.pojo.po.UserPo;
import cn.huava.cloud.common.validation.BaseValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 删除用户前的校验器，与 {@link BeforeDeleteUser} 注解配合使用
 *
 * @author Camio1945
 */
public class BeforeDeleteUserValidator extends BaseValidator
    implements ConstraintValidator<BeforeDeleteUser, UserPo> {

  @Override
  public boolean isValid(UserPo userPo, ConstraintValidatorContext context) {
    Long id = userPo.getId();
    if (id == null) {
      return customMessage(context, "用户ID不能为空");
    }
    if (id == ADMIN_USER_ID) {
      return customMessage(context, "该用户为重要用户，不允许删除");
    }
    return true;
  }
}
