package cn.huava.cloud.admin.validation.user;

import cn.huava.cloud.admin.pojo.po.UserPo;
import cn.huava.cloud.admin.pojo.qo.UpdatePasswordQo;
import cn.huava.cloud.admin.util.Fn;
import cn.huava.cloud.common.validation.BaseValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户密码校验器，与 {@link Password} 注解配合使用
 *
 * @author Camio1945
 */
public class PasswordValidator extends BaseValidator
    implements ConstraintValidator<Password, UpdatePasswordQo> {

  @Override
  public boolean isValid(UpdatePasswordQo updatePasswordQo, ConstraintValidatorContext context) {
    String oldPassword = updatePasswordQo.getOldPassword();
    String newPassword = updatePasswordQo.getNewPassword();
    if (Fn.isBlank(oldPassword) || Fn.isBlank(newPassword)) {
      return true;
    }
    if (oldPassword.equals(newPassword)) {
      return customMessage(context, "新密码不能与旧密码相同");
    }
    UserPo loginUser = Fn.getLoginUser();
    PasswordEncoder passwordEncoder = Fn.getBean(PasswordEncoder.class);
    if (!passwordEncoder.matches(updatePasswordQo.getOldPassword(), loginUser.getPassword())) {
      return customMessage(context, "旧密码不正确");
    }
    return true;
  }
}
