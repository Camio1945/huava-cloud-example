package cn.huava.cloud.admin.validation.user;

import static cn.huava.cloud.admin.constant.AdminConstant.MAX_PASSWORD_LENGTH;
import static cn.huava.cloud.admin.constant.AdminConstant.MIN_PASSWORD_LENGTH;
import static cn.huava.cloud.common.constant.CommonConstant.*;

import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.admin.pojo.po.UserPo;
import cn.huava.cloud.common.validation.BaseValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 更新用户前的校验器，与 {@link BeforeUpdateUser} 注解配合使用
 *
 * @author Camio1945
 */
public class BeforeUpdateUserValidator extends BaseValidator
    implements ConstraintValidator<BeforeUpdateUser, UserPo> {

  @Override
  public boolean isValid(UserPo userPo, ConstraintValidatorContext context) {
    Long id = userPo.getId();
    if (id == null) {
      return true;
    }
    if (id == ADMIN_USER_ID) {
      return customMessage(context, "该用户为重要用户，不允许删除");
    }
    if (Fn.isNotBlank(userPo.getPassword())) {
      return validatePassword(context, userPo.getPassword());
    }
    return true;
  }

  private boolean validatePassword(ConstraintValidatorContext context, String password) {
    int len = password.length();
    int min = MIN_PASSWORD_LENGTH;
    int max = MAX_PASSWORD_LENGTH;
    if (len < min || len > max) {
      return customMessage(context, Fn.format("密码长度应该为 {} ~ {} 个字符", min, max));
    }
    return true;
  }
}
