package cn.huava.cloud.admin.validation.role;

import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.admin.pojo.po.RolePo;
import cn.huava.cloud.admin.service.role.AceRoleService;
import cn.huava.cloud.common.validation.BaseValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 创建角色时的唯一性校验器，与 {@link UniqueRoleName} 注解配合使用
 *
 * @author Camio1945
 */
public class UniqueRoleNameValidator extends BaseValidator
    implements ConstraintValidator<UniqueRoleName, RolePo> {

  @Override
  public boolean isValid(RolePo rolePo, ConstraintValidatorContext context) {
    String name = rolePo.getName();
    // 如果名称为空，其他校验器会生效，不需要在这里做处理
    if (Fn.isBlank(name)) {
      return true;
    }
    boolean isUpdate = basicValidate(rolePo);
    Long id = isUpdate ? rolePo.getId() : null;
    return !Fn.getBean(AceRoleService.class).isNameExists(id, name);
  }
}
