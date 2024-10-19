package cn.huava.cloud.admin.validation.role;

import static cn.huava.cloud.common.constant.CommonConstant.ADMIN_ROLE_ID;

import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.admin.pojo.po.RolePo;
import cn.huava.cloud.admin.service.userrole.AceUserRoleService;
import cn.huava.cloud.common.validation.BaseValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 删除角色前的校验器，与 {@link BeforeDeleteRole} 注解配合使用
 *
 * @author Camio1945
 */
public class BeforeDeleteRoleValidator extends BaseValidator
    implements ConstraintValidator<BeforeDeleteRole, RolePo> {

  @Override
  public boolean isValid(RolePo rolePo, ConstraintValidatorContext context) {
    Long id = rolePo.getId();
    if (id == null) {
      return customMessage(context, "角色ID不能为空");
    }
    if (id == ADMIN_ROLE_ID) {
      return customMessage(context, "该角色为重要角色，不允许删除");
    }
    AceUserRoleService userRoleService = Fn.getBean(AceUserRoleService.class);
    long userCount = userRoleService.countUserByRoleId(id);
    if (userCount > 0) {
      return customMessage(context, "角色下存在用户，不能删除");
    }
    return true;
  }
}
