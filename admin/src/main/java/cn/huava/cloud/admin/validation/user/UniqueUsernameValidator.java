package cn.huava.cloud.admin.validation.user;

import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.admin.pojo.po.UserPo;
import cn.huava.cloud.admin.service.user.AceUserService;
import cn.huava.cloud.common.validation.BaseValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 用户名唯一性校验器，与 {@link UniqueUsername} 注解配合使用
 *
 * @author Camio1945
 */
public class UniqueUsernameValidator extends BaseValidator
    implements ConstraintValidator<UniqueUsername, UserPo> {

  @Override
  public boolean isValid(UserPo userPo, ConstraintValidatorContext context) {
    String username = userPo.getUsername();
    // 如果用户名为空，其他校验器会生效，不需要在这里做处理
    if (Fn.isBlank(username)) {
      return true;
    }
    boolean isUpdate = basicValidate(userPo);
    Long id = isUpdate ? userPo.getId() : null;
    return !Fn.getBean(AceUserService.class).isUsernameExists(id, username);
  }
}
