package cn.huava.cloud.member.validation;

import cn.huava.cloud.member.util.Fn;
import cn.huava.cloud.common.validation.BaseValidator;
import cn.huava.cloud.member.pojo.po.MemberPo;
import cn.huava.cloud.member.service.member.AceMemberService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 用户名唯一性校验器，与 {@link UniqueUsername} 注解配合使用
 *
 * @author Camio1945
 */
public class UniqueUsernameValidator extends BaseValidator
    implements ConstraintValidator<UniqueUsername, MemberPo> {

  @Override
  public boolean isValid(MemberPo registerPo, ConstraintValidatorContext context) {
    String username = registerPo.getUsername();
    // 如果用户名为空，其他校验器会生效，不需要在这里做处理
    if (Fn.isBlank(username)) {
      return true;
    }
    boolean isUpdate = basicValidate(registerPo);
    Long id = isUpdate ? registerPo.getId() : null;
    return !Fn.getBean(AceMemberService.class).isUsernameExists(id, username);
  }
}
