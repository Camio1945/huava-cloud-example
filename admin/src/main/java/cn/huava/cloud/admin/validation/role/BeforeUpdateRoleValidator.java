package cn.huava.cloud.admin.validation.role;

import static cn.huava.cloud.common.constant.CommonConstant.ADMIN_ROLE_ID;

import cn.huava.cloud.admin.pojo.po.RolePo;
import cn.huava.cloud.common.validation.BaseValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 更新角色前的校验器，与 {@link BeforeUpdateRole} 注解配合使用
 *
 * @author Camio1945
 */
public class BeforeUpdateRoleValidator extends BaseValidator
    implements ConstraintValidator<BeforeUpdateRole, RolePo> {

  @Override
  public boolean isValid(RolePo rolePo, ConstraintValidatorContext context) {
    Long id = rolePo.getId();
    if (id == null) {
      return true;
    }
    if (id == ADMIN_ROLE_ID) {
      return customMessage(context, "该角色为重要角色，不允许删除");
    }
    return true;
  }
}
